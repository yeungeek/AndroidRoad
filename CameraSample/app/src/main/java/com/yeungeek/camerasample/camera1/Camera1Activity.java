package com.yeungeek.camerasample.camera1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yeungeek.camerasample.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @date 2019-10-19
 */

public class Camera1Activity extends AppCompatActivity implements Camera.PreviewCallback, View.OnClickListener {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    //    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Camera mCamera;
    private Camera.Parameters mParameters;


    private int mWidth;
    private int mHeight;

    private File mPicturesPath = null;
    private int mOrientation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_1);

        initViews();
        init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    private void initViews() {
        mSurfaceView = findViewById(R.id.camera_surface_view);
        findViewById(R.id.camera_take_picture).setOnClickListener(this);
    }

    private void init() {
        mPicturesPath = getExternalCacheDir();

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (null == mCamera) {
                    openCamera(mCameraId);
                }

                startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCamera();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;

        Log.d("DEBUG", "##### init screen width: " + mWidth + ", height: " + mHeight);
    }


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_take_picture:
                takePicture();
                break;
        }
    }

    private void takePicture() {
        if (null != mCamera) {
            mCamera.takePicture(new Camera.ShutterCallback() {
                @Override
                public void onShutter() {

                }
            }, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    //base data
                }
            }, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(final byte[] data, Camera camera) {
                    mCamera.startPreview();

                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    savePhoto(data);
                                }
                            }
                    ).start();
                }
            });
        }
    }

    private void savePhoto(final byte[] data) {
        if (!mPicturesPath.exists()) {
            mPicturesPath.mkdirs();
        }

        File file = new File(mPicturesPath, System.currentTimeMillis() + ".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        rotateImageView(mCameraId, mOrientation, file.getPath());
    }

    private void rotateImageView(int cameraId, int orientation, String path) {
        Log.d("DEBUG", "##### save path: " + path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);


        Bitmap resizeBitmap = null;
        switch (cameraId) {
            case Camera.CameraInfo.CAMERA_FACING_BACK:
                Matrix matrix = new Matrix();
                if (mOrientation == 90) {
                    matrix.postRotate(90);
                }
                resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        matrix, true);
                break;
            case Camera.CameraInfo.CAMERA_FACING_FRONT:
                Matrix m = new Matrix();
                m.postScale(-1f, 1f);
                resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        m, true);
                break;
        }

        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            resizeBitmap.recycle();


        } catch (Exception e) {
            e.printStackTrace();
        }

        Looper.prepare();
        Toast.makeText(Camera1Activity.this, "save file path: " + path, Toast.LENGTH_LONG).show();
        Looper.loop();
    }


    private void openCamera(final int cameraId) {
        if (isSupport(cameraId)) {
            try {
                mCamera = Camera.open(cameraId);
                initParameters(mCamera);

                //preview
                if (null != mCamera) {
                    mCamera.setPreviewCallback(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initParameters(final Camera camera) {
        mParameters = camera.getParameters();
        mParameters.setPreviewFormat(ImageFormat.NV21); //default

        if (isSupportFocus(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (isSupportFocus(Camera.Parameters.FOCUS_MODE_AUTO)) {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        setPreviewSize();
        setPictureSize();

        camera.setParameters(mParameters);
    }

    private void startPreview() {
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            setCameraDisplayOrientation();
            mCamera.startPreview();

            startFaceDetect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startFaceDetect() {
        mCamera.startFaceDetection();
        mCamera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
            @Override
            public void onFaceDetection(Camera.Face[] faces, Camera camera) {
                Log.d("DEBUG", "##### face length: " + faces.length);
            }
        });
    }

    private void setPreviewSize() {
        List<Camera.Size> supportSizes = mParameters.getSupportedPreviewSizes();
        Camera.Size biggestSize = null;
        Camera.Size fitSize = null;
        Camera.Size targetSize = null;
        Camera.Size targetSiz2 = null;

        if (null != supportSizes) {
            for (int i = 0; i < supportSizes.size(); i++) {
                Camera.Size size = supportSizes.get(i);
                Log.d("DEBUG", "##### support size width: " + size.width + ", height: " + size.height);

                if (biggestSize == null ||
                        (size.width >= biggestSize.width && size.height >= biggestSize.height)) {
                    biggestSize = size;
                }

                if (size.width == mWidth
                        && size.height == mHeight) {
                    fitSize = size;
                    //如果任一宽或者高等于所支持的尺寸
                } else if (size.width == mWidth
                        || size.height == mHeight) {
                    if (targetSize == null) {
                        targetSize = size;
                        //如果上面条件都不成立 如果任一宽高小于所支持的尺寸
                    } else if (size.width < mWidth
                            || size.height < mHeight) {
                        targetSiz2 = size;
                    }
                }
            }


            if (fitSize == null) {
                fitSize = targetSize;
            }

            if (fitSize == null) {
                fitSize = targetSiz2;
            }

            if (fitSize == null) {
                fitSize = biggestSize;
            }

            Log.d("DEBUG", "##### fitSize width: " + fitSize.width + ", height: " + fitSize.height);
            mParameters.setPreviewSize(fitSize.width, fitSize.height);
        }
    }

    /**
     * save picture size
     */
    private void setPictureSize() {
        List<Camera.Size> mPictureSizes = mParameters.getSupportedPictureSizes();
        Camera.Size previewSize = mParameters.getPreviewSize();
        Camera.Size biggestSize = null;
        Camera.Size fitSize = null;// 优先选预览界面的尺寸
        Log.d("DEBUG", "##### preview size: " + previewSize.width + ", height:" + previewSize.height);

        float scaleSize = 0;
        if (null != previewSize) {
            scaleSize = previewSize.width / (float) previewSize.height;
        }

        for (int i = 0; i < mPictureSizes.size(); i++) {
            Camera.Size picture = mPictureSizes.get(i);
            Log.d("DEBUG", "##### picture size width: " + picture.width + ", height: " + picture.height);
            if (null == biggestSize) {
                biggestSize = picture;
            } else if (picture.width > biggestSize.width && picture.height > biggestSize.height) {
                biggestSize = picture;
            }

            if (scaleSize > 0 && picture.width > previewSize.width && picture.height > previewSize.height) {
                float currentScale = picture.width / (float) picture.height;
                if (scaleSize == currentScale) {
                    if (null == fitSize) {
                        fitSize = picture;
                    } else if (picture.width > fitSize.width && picture.height > fitSize.height) {
                        fitSize = picture;
                    }
                }
            }
        }

        if (null == fitSize) {
            fitSize = biggestSize;
        }

        Log.d("DEBUG", "##### fit size: " + fitSize.width + ", height:" + fitSize.height);
        mParameters.setPictureSize(fitSize.width, fitSize.height);
    }

    private void releaseCamera() {
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.stopFaceDetection();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private void setCameraDisplayOrientation() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, cameraInfo);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();  //自然方向
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        //cameraInfo.orientation 图像传感方向
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (cameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (cameraInfo.orientation - degrees + 360) % 360;
        }
        Log.d("DEBUG", "##### setCameraDisplayOrientation rotation: " + rotation
                + ", cameraInfo.orientation: " + cameraInfo.orientation + ", result: " + result);

        mOrientation = result;
        //相机预览方向
        mCamera.setDisplayOrientation(result);
    }

    private boolean isSupport(int backOrFront) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == backOrFront) {
                return true;
            }
        }
        return false;
    }

    private boolean isSupportFocus(String focusMode) {
        for (String mode : mParameters.getSupportedFocusModes()) {
            if (focusMode.equals(mode)) {
                return true;
            }
        }

        return false;
    }
}
