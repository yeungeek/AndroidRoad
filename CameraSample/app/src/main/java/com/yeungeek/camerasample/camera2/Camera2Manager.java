package com.yeungeek.camerasample.camera2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Surface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Author: Zack
 * Date:   2020.06.20 16:20
 */
public class Camera2Manager {

    private static final String TAG = Camera2Manager.class.getSimpleName();


    private Context mContext;

    private CameraManager mCameraManager;

    private String mCameraId;

    private HandlerThread mCameraThread;
    private Handler mCameraHandler;

    private HandlerThread mImageThread;
    private Handler mImageHandler;

    private ImageReader mImageReader;

    private CameraDevice mCameraDevice;

    private CameraCharacteristics mCameraCharacteristics;

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private Size mPreviewSize = new Size(1920, 1080);

//    private byte[] mBuffer;

    private CaptureRequest.Builder mPreviewRequestBuilder;

    private CameraCaptureSession mCaptureSession;

    private int mFacing = CameraCharacteristics.LENS_FACING_BACK;

    private FrameCallback mFrameCallback;

    private SurfaceTexture mSurfaceTexture;
    private SurfaceTexture mSurfaceTexture1;

    private ImageReader.OnImageAvailableListener mImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireNextImage();
//            mBuffer = convertYUV420888ToNv21(image);
//            if (mFrameCallback != null) {
            Log.d(TAG, "##### onFrame: " + image.getPlanes());
//                mFrameCallback.onFrame(image);
//            }
            image.close();
        }
    };

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraOpenCloseLock.release();
            mCameraDevice = camera;
            startCaptureSession();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.e("DEBUG", "onError: " + error);
            mCameraOpenCloseLock.release();
            camera.close();
            mCameraDevice = null;
            //多路不出数据，这里重启相机
            Log.e("DEBUG", "onError:  restart camera");
            stopPreview();
            startPreview();
        }
    };


//    public Camera2Manager(Context context) {
//       this(context);
//    }

    public Camera2Manager(Context context) {
        mContext = context;
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    public void startPreview() {
        if (!chooseCameraIdByFacing()) {
            Log.e(TAG, "Choose camera failed.");
            return;
        }

        mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());

        mImageThread = new HandlerThread("ImageThread");
        mImageThread.start();
        mImageHandler = new Handler(mImageThread.getLooper());

        prepareImageReader();
        openCamera();
    }

    public void stopPreview() {
        closeCamera();
        if (mCameraThread != null) {
            mCameraThread.quitSafely();
            mCameraThread = null;
        }
        mCameraHandler = null;

        if (mImageThread != null) {
            mImageThread.quitSafely();
            mImageThread = null;
        }
        mImageHandler = null;

//        mBuffer = null;
    }

    private void prepareImageReader() {

        if (mImageReader != null) {
            mImageReader.close();
        }
        mImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.YUV_420_888, 1);
        mImageReader.setOnImageAvailableListener(mImageAvailableListener, mImageHandler);
    }

    private static Size chooseOptimalSize(Size[] choices, int textureWidth, int textureHeight,
                                          int maxWidth, int maxHeight, Size aspectRatio) {

        List<Size> bigEnough = new ArrayList<>();

        List<Size> notBigEnough = new ArrayList<>();

        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();

        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getWidth() == option.getHeight() * w / h) {
                if (option.getWidth() >= textureWidth && option.getHeight() >= textureHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        }
        return choices[0];
    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size o1, Size o2) {
            return Long.signum(o1.getWidth() * o1.getHeight() - o2.getWidth() * o2.getHeight());
        }
    }

    private boolean chooseCameraIdByFacing() {
        try {
            String ids[] = mCameraManager.getCameraIdList();
            if (ids.length == 0) {
                Log.e(TAG, "No available camera.");
                return false;
            }

            for (String cameraId : mCameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                Integer level = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                if (level == null || level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                    continue;
                }

                Integer internal = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (internal == null) {
                    continue;
                }
                if (internal == mFacing) {
                    mCameraId = cameraId;
                    mCameraCharacteristics = characteristics;
                    return true;
                }
            }
            mCameraId = ids[1];
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            Integer level = mCameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
            if (level == null || level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                return false;
            }

            Integer internal = mCameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (internal == null) {
                return false;
            }
            mFacing = CameraCharacteristics.LENS_FACING_BACK;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return true;
    }


    @SuppressLint("MissingPermission")
    public void openCamera() {
//        configureTransform(previewSize);
        if (TextUtils.isEmpty(mCameraId)) {
            Log.e(TAG, "Open camera failed. No camera available");
            return;
        }

        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            mCameraManager.openCamera(mCameraId, mStateCallback, mCameraHandler);
        } catch (InterruptedException | CameraAccessException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (mCaptureSession != null) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (mImageReader != null) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    private void configureTransform(int width, int height) {

    }

    private void startCaptureSession() {
        if (mCameraDevice == null) {
            return;
        }

        if ((mImageReader != null || mSurfaceTexture != null)) {
            try {
                mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                mPreviewRequestBuilder.addTarget(mImageReader.getSurface());
//                Zoom zoom = new Zoom(mCameraCharacteristics);
//                zoom.setZoom(mPreviewRequestBuilder, zoom.maxZoom  / 10);
                List<Surface> surfaceList = Arrays.asList(mImageReader.getSurface());
                if (mSurfaceTexture != null) {
                    // We configure the size of default buffer to be the size of camera preview we want.
                    mSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                    mSurfaceTexture1.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

                    Surface surface = new Surface(mSurfaceTexture);
                    Surface surface1 = new Surface(mSurfaceTexture1);
                    mPreviewRequestBuilder.addTarget(mImageReader.getSurface());
                    mPreviewRequestBuilder.addTarget(surface);
                    mPreviewRequestBuilder.addTarget(surface1);
                    surfaceList = Arrays.asList(surface, surface1, mImageReader.getSurface());
                } else {
//                    mPreviewRequestBuilder.addTarget(mImageReader.getSurface());
                }

                Range<Integer>[] fpsRanges = mCameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
                Log.d("DEBUG", "##### fpsRange: " + Arrays.toString(fpsRanges));

//                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, fpsRanges[3]);

                mCameraDevice.createCaptureSession(surfaceList, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(CameraCaptureSession session) {
                        if (mCameraDevice == null) return;

                        //二代关闭自动对焦  : CaptureRequest.CONTROL_AF_MODE_OFF
                        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_OFF);

                        mCaptureSession = session;
                        try {
                            if (mCaptureSession != null)
                                mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, null);
                        } catch (CameraAccessException | IllegalArgumentException | IllegalStateException | NullPointerException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onConfigureFailed(CameraCaptureSession session) {
                        Log.e(TAG, "Failed to configure capture session");
                    }

                    @Override
                    public void onClosed(CameraCaptureSession session) {
                        if (mCaptureSession != null && mCaptureSession.equals(session)) {
                            mCaptureSession = null;
                        }
                    }
                }, mCameraHandler);
            } catch (CameraAccessException e) {
//            e.printStackTrace();
                Log.e(TAG, e.getMessage());
            } catch (IllegalStateException e) {
                stopPreview();
                startPreview();
            } catch (UnsupportedOperationException e) {
//            e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
        }

    }


//    private byte[] convertYUV420888ToNv21(final Image image) {
//        if(image == null) return null;
//
//        Image.Plane[] planes = image.getPlanes();
//        if(planes.length == 0) return mBuffer;
//
//        ByteBuffer bufY = planes[0].getBuffer();
//        int size = image.getWidth() * image.getHeight();
//        int len = size * 3 / 2;
//        if(mBuffer == null || mBuffer.length != len) {
//            mBuffer = new byte[len];
//        }
//        bufY.get(mBuffer, 0, size);
//        ByteBuffer bufVU = planes[2].getBuffer();
//        bufVU.get(mBuffer, size, bufVU.remaining());
//        return mBuffer;
//    }

    public interface FrameCallback {
        void onFrame(Image data);
    }

    public Size getPreviewSize() {
        return mPreviewSize;
    }

    public void setPreviewSize(Size previewSize) {
        mPreviewSize = previewSize;
    }

    public FrameCallback getFrameCallback() {
        return mFrameCallback;
    }

    public void setFrameCallback(FrameCallback frameCallback) {
        mFrameCallback = frameCallback;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        mSurfaceTexture = surfaceTexture;
    }

    public void setSurfaceTexture1(SurfaceTexture surfaceTexture) {
        mSurfaceTexture1 = surfaceTexture;
    }
}
