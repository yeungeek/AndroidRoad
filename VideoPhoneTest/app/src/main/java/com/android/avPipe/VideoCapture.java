/*
** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
*/
/*============================================================================
**
**  FILE        VideoCapture.java
**
**  PURPOSE     Implement video capture using android camera interface
**
**==========================================================================*/

package com.android.avPipe;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;


/**
 * video capture
 *
 * <p>
 */
public class VideoCapture implements SurfaceHolder.Callback {

    public interface VideoEncodeSync {
        public void onVideoCaptureStarted();
        public void onVideoCaptureFrame(byte[] data, long timestamp);
    }

    private static final String TAG = "VideoCapture";
    private static VideoCapture mInstance = null;
    private VideoEncodeSync mEncodeSync = null;
    private SurfaceView mVideoView;
    private CameraInstance mCameraInstance;
    private int mPreviewWidth  = 240;
    private int mPreviewHeight = 180;
    private int mEncodeWidth  = 640;
    private int mEncodeHeight = 480;
    private int mfps = 0;
    private int mWidth;
    private int mHeight;
    boolean mIsStarted = false;
    boolean mShouldStartCapture = false;
    private boolean mUseMetaMode = false;  // AML brian
    private int mMetaDataLen = 0; // AML brian
    private String KeyPreviewCallBackInMetaData = "preview-callback-in-metadata-enable";
    private String KeyPreviewCallBackInMetaDataLenth = "preview-callback-in-metadata-length";

    private VideoCapture() {
    }

    public boolean isStarted() {
        return mIsStarted;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mShouldStartCapture) {
            startCameraPreview(holder, mWidth, mHeight, mfps);
            mShouldStartCapture = false;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "VideoCapture.surfaceDestroyed() " + holder);
        if (mCameraInstance != null) {
            mCameraInstance.stopPreview();
            mCameraInstance.close();
            mCameraInstance = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG, "VideoCapture.surfaceChanged() " + holder + " size " + width + "x" + height);
    }

    public synchronized void setEncoderCallback(VideoEncodeSync sync) {
        if (mIsStarted) {
            Log.e(TAG, "Ignoring encode mode, capture already started.");
            return;
        }
        mEncodeSync = sync;
    }

    // set video size to use for just preview without encoding
    public void setPreviewSize(int w, int h) {
        if (mIsStarted) {
            Log.e(TAG, "Ignoring setPreviewSize, capture already started.");
            return;
        }
        mPreviewWidth  = w;
        mPreviewHeight = h;
    }

    // set video size that will be used for encoding
    public synchronized void setEncodeSize(int w, int h) {
        if (mIsStarted) {
            Log.e(TAG, "Ignoring setEncodeSize, capture already started.");
            return;
        }

        mEncodeWidth  = w;
        mEncodeHeight = h;
    }

    public synchronized void setTextureView(SurfaceView view) {
        if (mIsStarted) {
            Log.e(TAG, "Ignoring setTextureView, capture already started.");
            return;
        }

        if (mVideoView != null) {
            mVideoView.getHolder().removeCallback(this);
        }
        mVideoView = view;
        if (mVideoView != null) {
            mVideoView.getHolder().addCallback(this);
        }
    }

    public static VideoCapture Instance() {
        if (mInstance == null) {
            mInstance = new VideoCapture();
        }
        return mInstance;
    }

    public Camera getCamera() {
        return (mCameraInstance == null) ? null : mCameraInstance.getCamera();
    }

    public synchronized boolean openCamera(boolean frontFacing) {
      if (mCameraInstance == null) {
            mCameraInstance = new CameraInstance();
            if (mCameraInstance.open(frontFacing) != true) {
                mCameraInstance = null;
                return false;
            }
        }
        return true;
    }

    public synchronized void closeCamera() {
        if (mCameraInstance != null) {
            stop();
            mCameraInstance.close();
            mCameraInstance = null;
        }
    }

    public synchronized void startCapture() {
        if (openCamera(true) != true)
            return;

        if (mEncodeSync != null) {
            mWidth = mEncodeWidth;
            mHeight = mEncodeHeight;
        } else {
            mWidth = mPreviewWidth;
            mHeight = mPreviewHeight;
        }

        AVTypes.VideoFmt fmt = getClosestSupportedSize(mWidth, mHeight);
        mWidth = fmt.width;
        mHeight = fmt.height;

        setCameraParams();

        startCameraPreview(mVideoView.getHolder(), mWidth, mHeight, mfps);
        mIsStarted = true;
    }

    public synchronized void stop() {
        if (mCameraInstance != null) {
            mCameraInstance.stopPreview();
        }
        mIsStarted = false;
    }

    private AVTypes.VideoFmt getClosestSupportedSize(int width, int height) {
        AVTypes.VideoFmt[] vfmts = getSupportedFormats();
        AVTypes.VideoFmt best = null;

        for (int i = 0; i < vfmts.length; i++) {
            if ((vfmts[i].width >= width) && (vfmts[i].height >= height)) {
                if ((best == null) || ((vfmts[i].width <= best.width) && (vfmts[i].height <= best.height))) {
                    if (best == null) {
                        best = new AVTypes.VideoFmt();
                    }
                    best.width = vfmts[i].width;
                    best.height = vfmts[i].height;
                }
            }
        }

        if (best == null)
            best = vfmts[0];

        return best;
    }

    public AVTypes.VideoFmt[] getSupportedFormats() {
        if (mCameraInstance == null) {
            return null;
        }

        Parameters params = mCameraInstance.getCamera().getParameters();
        List<Size> sizes = params.getSupportedPreviewSizes();
        AVTypes.VideoFmt[] fmts = new AVTypes.VideoFmt[sizes.size()];
		Log.d(TAG, "getSupportedPreviewSizes:" + sizes.size());
        for (int i = 0; i < sizes.size(); ++i) {
            fmts[i] = new AVTypes.VideoFmt();
            fmts[i].width = sizes.get(i).width;
            fmts[i].height = sizes.get(i).height;
            fmts[i].framerate = 30;
            fmts[i].bitrate = getBitrateOfVideo(fmts[i].width, fmts[i].height);

            Log.d(TAG, "getSupportedFormats() -- " + fmts[i].width + "x" + fmts[i].height);
        }

        return fmts;
    }

    public int getBitrateOfVideo(int w, int h) {
        int bitrate = 0;

        if (w < 512) {
            bitrate = 128000;
        } else if (w < 640) {
            bitrate = 256000;
        } else if (w < 768) {
            bitrate = 384000;
        } else if (w < 960) {
            bitrate = 512000;
        } else if (w < 1168) {
            bitrate = 768000;
        } else if (w < 1440) {
            bitrate = 1024000;
        } else {
            bitrate = 2048000;
        }

        return bitrate;
    }


    /**
     * Camera parameter setting
     */
    private void setCameraParams() {
        mCameraInstance.configurePreview(mWidth, mHeight, ImageFormat.NV21);
    }

    public void setFrameRate(int fps) {
        mfps = fps;
    }
    private void startCameraPreview(SurfaceHolder holder, int width, int height, int fps) {
        if (mCameraInstance == null) {
            return;
        }

        // if the holder does not have a surface yet, set a flag to recall this method when it does.
        if (!holder.getSurface().isValid()) {
            Log.v(TAG, "---Surface not ready for VideoCapture output at time of start.\n Waiting on VideoCapture.onSurfaceCreated()\n");
            mShouldStartCapture = true;
            return;
        }

        Camera camera = getCamera();
        Parameters params = camera.getParameters();
        // buffer size calculation take from documentatoin for addCallbackBuffer
        int previewormat = params.getPreviewFormat();
        Size previewSize = params.getPreviewSize();
        Log.e(TAG,"videocap fps = " + fps);
        Log.e(TAG,"videocap previewormat = " + previewormat);
        if (fps != 0) {
            params.setPreviewFrameRate(fps);
        }
        int dataBufferSize;
        if (previewormat == ImageFormat.YV12) {
          int yStride = (previewSize.width + 15) / 16 * 16;
          int uvStride = ((yStride / 2) + 15) / 16 * 16;
          dataBufferSize = (yStride + uvStride) * previewSize.height;
        } else {
          dataBufferSize = previewSize.width * previewSize.height * ImageFormat.getBitsPerPixel(params.getPreviewFormat()) / 8;
        }
        String metadata = null;
        metadata = params.get(KeyPreviewCallBackInMetaData);
        if (metadata != null) {
            params.set(KeyPreviewCallBackInMetaData, 1);
            Log.d(TAG, "mCamera set preview in meta data mode :" + params.get(KeyPreviewCallBackInMetaData));
            mMetaDataLen = params.getInt(KeyPreviewCallBackInMetaDataLenth);
            Log.d(TAG, "mCamera meta data length :" + mMetaDataLen);
            mUseMetaMode = true;
            camera.setParameters(params);
        }
        if (mUseMetaMode == true)
            dataBufferSize = mMetaDataLen;
        Log.d(TAG, "dataBufferSize :" + dataBufferSize);
        mCameraInstance.setPreviewBuffers(dataBufferSize, 6, mEncodeSync);
        //mCameraInstance.setPreviewBuffers( previewSize.width * previewSize.height * 3 / 2, 6, mEncodeSync);
        mCameraInstance.setPreviewTarget(holder);
        mCameraInstance.startPreview();

        if (mEncodeSync != null) {
            mEncodeSync.onVideoCaptureStarted();
        }
    }
}
