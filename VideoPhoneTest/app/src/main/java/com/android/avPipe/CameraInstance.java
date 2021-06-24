/*
** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
*/
/*============================================================================
**
**  FILE        CameraInstance.java
**
**  PURPOSE     Implement a wrapper for a single instance of a camera.
**
**==========================================================================*/

package com.android.avPipe;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;

import com.android.avPipe.VideoCapture.VideoEncodeSync;

import java.io.IOException;

public class CameraInstance {
    private final String TAG = "CameraInstance";
    private Camera mCamera;
    private int mFacing = -1;
    private int mCameraID = -1;
    private boolean mPreviewStarted = false;
    private final boolean DEBUG = true;
    private VideoEncodeSync mEncodeSync; /* interface used to notify of new Camera frames */
    public CameraInstance() {
    }

    public boolean open(boolean front) {
        if (mCamera != null) {
            return true;
        }

        if (DEBUG) Log.v(TAG, "--- Camera Open :                       " + now());

        int direction = front ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
        Camera.CameraInfo info = new Camera.CameraInfo();
        int numCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numCameras; ++i) {
            Camera.getCameraInfo(i, info);
            if (direction == info.facing) {
                mCameraID = i;
                mFacing = direction;
                break;
            }
        }

        //FIXME: workaround for devices that misreport camera facing.
        //if we only have a single camera, set it as the one we will use and keep the direction desired.
        // once this works properly just remove the if (onecamera) case and the rest should work.
        if ((numCameras > 0) && (mCameraID < 0)) {
            Log.v(TAG, "***** Camera Workaround, direction incorrect!");
            mCameraID = 0;
            mFacing = direction;
        }

        if (mCameraID >= 0) {
            Log.v(TAG, "Opening camera id " + mCameraID);
            mCamera = Camera.open(mCameraID);
            if (DEBUG) Log.v(TAG, "--- Camera Open Complete :              " + now());
            return (mCamera != null) ? true : false;
        }

        Log.v(TAG, "No camera matching requested Camera Facing found");
        mFacing = -1;
        return false;
    }

    public void close() {
        if (mCamera != null) {
            if (DEBUG) Log.v(TAG, "--- Camera Close:                       " + now());
            if (mPreviewStarted)
                stopPreview();

            mCamera.release();
            if (DEBUG) Log.v(TAG, "--- Camera Close Complete:              " + now());
            mCamera = null;
        }
    }

    public void setPreviewBuffers(int bufferSize, int bufferCount, VideoEncodeSync callback) {
        if (DEBUG) Log.v(TAG, "--- Camera setPreviewBuffers:           " + now());
        if (!mPreviewStarted && mCamera != null) {
            for (int i=0; i<bufferCount; ++i) {
                mCamera.addCallbackBuffer(new byte[bufferSize]);
            }
            // store the interface for callbacks.  apply on startPreview()
            mEncodeSync = callback;
        }
        //if (DEBUG)
        Log.v(TAG, "--- Camera setPreviewBuffers Complete:  " + now());
    }

    public void setPreviewTarget(SurfaceHolder holder) {
        if (DEBUG) Log.v(TAG, "--- Camera setPreviewTarget:            " + now());
        if (!mPreviewStarted && (mCamera != null))
            try {
                //if (DEBUG)
                Log.v(TAG, "--- new camera surface : " + holder);
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                Log.v(TAG, "Failed to setPreviewTarget, io Exception\n");
                e.printStackTrace();
            }
        else
            Log.v(TAG, "Failed to setPreviewTarget on the CameraInstance\n");

        //if (DEBUG)
        Log.v(TAG, "--- Camera setPreviewTarget Complete:   " + now());
    }

    public void configurePreview(int width, int height, int pixelFormat) {
        if (mCamera != null) {
            if (DEBUG) Log.v(TAG, "--- Camera configurePreview             " + now());

            Parameters p = mCamera.getParameters();
            Camera.Size size = p.getPreviewSize();
            int format = p.getPreviewFormat();

            if (pixelFormat != format) {
                //if (DEBUG)
                Log.v(TAG, "--- new camera pixelformat : " + pixelFormat);
                p.setPreviewFormat(pixelFormat);
            }
            if ((size.width != width) || (size.height != height)) {
                //if (DEBUG)
                Log.v(TAG, "--- new camera preview size : " + width + "x" + height);
                p.setPreviewSize(width, height);
            }

            mCamera.setParameters(p);

            //if (DEBUG)
            Log.v(TAG, "--- Camera configurePreview Complete:   " + now());
        } else {
            Log.v(TAG, "No Camera to configure, open the camera instance first!\n");
        }
    }

    public void startPreview() {
        if (mCamera != null) {
            //if (DEBUG)
            Log.v(TAG, "--- Camera startPreview:                " + now());
            if (mEncodeSync != null) {
                Log.v(TAG, "--- Camera setting camera preview callback\n");
                mCamera.setPreviewCallbackWithBuffer(new CameraPreviewCallback());
            }

            mCamera.startPreview();
            mPreviewStarted = true;
            //if (DEBUG)
            Log.v(TAG, "--- Camera startPreview Complete:       " + now());
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            if (DEBUG) Log.v(TAG, "--- Camera stopPreview:                 " + now());
            mCamera.setPreviewCallbackWithBuffer(null);
            mCamera.stopPreview();
            if (DEBUG) Log.v(TAG, "--- Camera stopPreview Complete:        " + now());
            mPreviewStarted = false;
        }
    }

    private long now() { return System.nanoTime() / 1000000; }
    public Camera getCamera() { return mCamera; }
    public boolean isPreviewing() { return mPreviewStarted; }

    private class CameraPreviewCallback implements Camera.PreviewCallback {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            mEncodeSync.onVideoCaptureFrame(data, System.nanoTime());
            if (mCamera != null) {
                mCamera.addCallbackBuffer(data);
            }
        }
    }
}

