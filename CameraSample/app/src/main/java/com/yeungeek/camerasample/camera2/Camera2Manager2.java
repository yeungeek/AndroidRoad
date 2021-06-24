package com.yeungeek.camerasample.camera2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.SparseIntArray;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Camera2 Manager
 */
public class Camera2Manager2 {
    static int FACING_BACK = 0;
    static int FACING_FRONT = 1;

    private static final SparseIntArray INTERNAL_FACINGS = new SparseIntArray();

    static {
        INTERNAL_FACINGS.put(FACING_BACK, CameraCharacteristics.LENS_FACING_BACK);
        INTERNAL_FACINGS.put(FACING_FRONT, CameraCharacteristics.LENS_FACING_FRONT);
    }

    private CameraManager mCameraManager;
    private int mFacing;
    private String mCameraId;
    private CameraCharacteristics mCameraCharacteristics;
    CameraDevice mCamera;
    CameraCaptureSession mCaptureSession;
    CaptureRequest.Builder mPreviewRequestBuilder;

    private ImageReader mImageReader;
    final int imageW = 720;
    final int imageH = 1280;
    final float cameraLength = imageW * imageH * 1.5f;
    private byte[] mCameraData;
    private Handler backgroundHandler, mainHandler, imageHandler;

    private boolean isAutoFocus;

    private float mMaximumZoomLevel;
    private float mZoomLevel = 1f;
    private float mDelta = 0.05f;
    private Rect mZoom;

    private Camera2Manager2() {

    }

    private static final class Holder {
        private static final Camera2Manager2 INSTANCE = new Camera2Manager2();
    }

    public static Camera2Manager2 getInstance() {
        return Holder.INSTANCE;
    }

    public void init(final Context context) {
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        //TODO
        if (!chooseCameraIdByFacing()) {
            return;
        }
        HandlerThread handlerThread = new HandlerThread("camera2");
        handlerThread.start();
        HandlerThread imageThread = new HandlerThread("image reader");
        imageThread.start();
        imageHandler = new Handler(imageThread.getLooper());
        backgroundHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());

        prepareImageReader();
        startOpeningCamera();

        mFile = new File(context.getExternalFilesDir(null), "test.yuv");
        Log.d("DEBUG", "##### file path: " + mFile.getAbsolutePath());
    }

    public void destroy() {
        isAutoFocus = false;

        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
        if (mCamera != null) {
            mCamera.close();
            mCamera = null;
        }
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }

        imageHandler = null;
        backgroundHandler = null;
        mainHandler = null;
        mCameraData = null;
    }

    public void autoFocus() {
        if (!isAutoFocus) {
            autoOnMode();
        } else {
            autoOffMode();
        }
    }

    public boolean isAutoFocus() {
        return isAutoFocus;
    }

    private void autoOnMode() {
        if (null == mCaptureSession || null == mPreviewRequestBuilder) {
            return;
        }
        isAutoFocus = true;
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        setRepeatingRequest();
    }

    public void autoOffMode() {
        if (null == mCaptureSession || null == mPreviewRequestBuilder) {
            return;
        }
        isAutoFocus = false;
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_OFF);
        setRepeatingRequest();
    }

    private void setRepeatingRequest() {
        try {
            mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(),
                    null, backgroundHandler);
        } catch (CameraAccessException e) {
            Log.e("DEBUG", "Failed to start camera preview because it couldn't access camera", e);
        } catch (IllegalStateException e) {
            Log.e("DEBUG", "Failed to start camera preview.", e);
        }
    }

    private boolean chooseCameraIdByFacing() {
        try {
            int internalFacing = INTERNAL_FACINGS.get(mFacing);
            final String[] ids = mCameraManager.getCameraIdList();
            if (ids.length == 0) { // No camera
                throw new RuntimeException("No camera available.");
            }
            for (String id : ids) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(id);
                //TODO: remove get HARDWARE_LEVEL
//                Integer level = characteristics.get(
//                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
//                Logger.d("##### HARDWARE_LEVEL: %s", level);
//                if (level == null ||
//                        level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
//                    continue;
//                }
                Integer internal = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (internal == null) {
                    throw new NullPointerException("Unexpected state: LENS_FACING null");
                }
                if (internal == internalFacing) {
                    mCameraId = id;
                    mCameraCharacteristics = characteristics;

                    //TODO
                    mMaximumZoomLevel = (characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM));
                    return true;
                }
            }
            // Not found
            mCameraId = ids[0];
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId);

            //TODO
            mMaximumZoomLevel = (mCameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM));

//            Integer level = mCameraCharacteristics.get(
//                    CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
//            if (level == null ||
//                    level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
//                return false;
//            }

            Integer internal = mCameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (internal == null) {
                throw new NullPointerException("Unexpected state: LENS_FACING null");
            }
            for (int i = 0, count = INTERNAL_FACINGS.size(); i < count; i++) {
                if (INTERNAL_FACINGS.valueAt(i) == internal) {
                    mFacing = INTERNAL_FACINGS.keyAt(i);
                    return true;
                }
            }
            // The operation can reach here when the only camera device is an external one.
            // We treat it as facing back.
            mFacing = FACING_BACK;
            return true;
        } catch (CameraAccessException e) {
            throw new RuntimeException("Failed to get a list of camera devices", e);
        }
    }

    private void prepareImageReader() {
        if (mImageReader != null) {
            mImageReader.close();
        }
        mImageReader = ImageReader.newInstance(imageW, imageH, ImageFormat.YUV_420_888, 1);
        mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, imageHandler);
    }

    @SuppressLint("MissingPermission")
    private void startOpeningCamera() {
        try {
            mCameraManager.openCamera(mCameraId, mCameraDeviceCallback, mainHandler);
        } catch (CameraAccessException e) {
            //TODO
            throw new RuntimeException("Failed to open camera: " + mCameraId, e);
        }
    }

    void startCaptureSession() {
        if (!isCameraOpened() || mImageReader == null) {
            return;
        }

        try {
            mPreviewRequestBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(mImageReader.getSurface());
            mCamera.createCaptureSession(Arrays.asList(mImageReader.getSurface()),
                    mSessionCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            throw new RuntimeException("Failed to start camera session");
        }
    }

    boolean isCameraOpened() {
        return mCamera != null;
    }

    private File mFile;
    FileOutputStream output = null;
    ByteBuffer buffer;
    byte[] bytes;
    boolean success = false;

    //Listener
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = reader -> {
        Image image = reader.acquireNextImage();
        Log.d("DEBUG","###### onImage");
        image.close();
//        try {

//                output = new FileOutputStream(mFile);
//                for (int i = 0; i < 3; i++) {
//                    buffer = image.getPlanes()[i].getBuffer();
//                    bytes = new byte[buffer.remaining()]; // makes byte array large enough to hold image
//                    buffer.get(bytes); // copies image from buffer to byte array
//                    output.write(bytes);    // write the byte array to file
//                }
//                success = true;
//            }
//            if (!success) {
//                mCameraData = getDateFromImage(image);
//                dumpFile(mFile, mCameraData);
//                success = true;
//                if (mCameraData.length <= cameraLength) {
//                    //data
//                    image.close();
//                    Log.d("DEBUG", "##### mCameraData");
//                } else {
//                    Log.d("DEBUG", "##### buffer length error");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            image.close();
//        }
    };

    private void dumpFile(File file, byte[] data) {
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(file);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to create output file " + file.getName(), ioe);
        }
        try {
            outStream.write(data);
            outStream.close();
        } catch (IOException ioe) {
            throw new RuntimeException("failed writing data to file " + file.getName(), ioe);
        }
    }

    private final CameraDevice.StateCallback mCameraDeviceCallback
            = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCamera = camera;
//            mCallback.onCameraOpened();
            startCaptureSession();
        }

        @Override
        public void onClosed(@NonNull CameraDevice camera) {
//            mCallback.onCameraClosed();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCamera = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.e("DEBUG", "onError: " + camera.getId() + " (" + error + ")");
            if (mCaptureSession != null) {
                try {
                    mCaptureSession.stopRepeating();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (mCamera != null) {
                mCamera.close();
                mCamera = null;
            }
        }
    };

    private final CameraCaptureSession.StateCallback mSessionCallback
            = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            if (mCamera == null) {
                return;
            }
            mCaptureSession = session;
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            try {
                mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(),
                        null, backgroundHandler);
            } catch (CameraAccessException e) {
                Log.e("DEBUG", "Failed to start camera preview because it couldn't access camera", e);
            } catch (IllegalStateException e) {
                Log.e("DEBUG", "Failed to start camera preview.", e);
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.e("DEBUG", "Failed to configure capture session.");
        }

        @Override
        public void onClosed(@NonNull CameraCaptureSession session) {
            if (mCaptureSession != null && mCaptureSession.equals(session)) {
                mCaptureSession = null;
            }
        }
    };

    private byte[] getCameraData(final Image image) {
        Image.Plane[] planes = image.getPlanes();
        int byteLen = 0;
        for (Image.Plane plane : planes) {
            byteLen += plane.getBuffer().remaining();
        }

        byte[] bytes = new byte[byteLen];
        int offset = 0;
        for (Image.Plane plane : planes) {
            ByteBuffer buffer = plane.getBuffer();
            int remain = buffer.remaining();
            buffer.get(bytes, offset, remain);
            offset += remain;
        }

        return bytes;
    }

    private byte[] getDateFromImage(final Image image) {
        if (image != null) {
            Image.Plane[] planes = image.getPlanes();
            if (planes.length > 0) {
                ByteBuffer buffer = planes[0].getBuffer();
                int size = image.getWidth() * image.getHeight();
                int bufferSize = (size * 3) >> 1;
                if (mCameraData == null || mCameraData.length != bufferSize) {
                    mCameraData = new byte[bufferSize];
                }

                buffer.get(mCameraData, 0, size);
                ByteBuffer buffer2 = planes[1].getBuffer();
                buffer2.get(mCameraData, size, buffer2.remaining());
                return mCameraData;
            }
            return mCameraData;
        }
        return null;
    }
}
