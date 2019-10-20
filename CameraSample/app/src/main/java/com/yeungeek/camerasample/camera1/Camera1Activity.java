package com.yeungeek.camerasample.camera1;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yeungeek.camerasample.R;

import java.io.IOException;

/**
 * @date 2019-10-19
 */

public class Camera1Activity extends AppCompatActivity implements Camera.PreviewCallback {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

//    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private Camera mCamera;
    private Camera.Parameters mParameters;


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
    }

    private void init() {
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
    }


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

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

        camera.setParameters(mParameters);
    }

    private void startPreview() {
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
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

    private void releaseCamera() {
        mCamera.stopPreview();
        mCamera.stopFaceDetection();
        mCamera.setPreviewCallback(null);
        mCamera.release();
        mCamera = null;
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
