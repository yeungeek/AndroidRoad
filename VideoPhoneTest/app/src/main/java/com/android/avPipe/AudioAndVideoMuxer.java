package com.android.avPipe;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renjiang.Han on 2018/7/30.
 */
public class AudioAndVideoMuxer {
    private static final String TAG = "AudioAndVideoMuxer";
    private static final int MUXER_STATE_UNINITIALIZED  = -1;
    private static final int MUXER_STATE_INITIALIZED    = 0;
    private static final int MUXER_STATE_STARTED        = 1;
    private static final int MUXER_STATE_STOPPED        = 2;
    private String mEncoderCaptureFilename = null;
    private String mEncoderCaptureFilename_ext = "/sdcard";
    private MediaMuxer mMediaMuxer = null;
    private long firstPTS = 0;
    private int trackCount = 0;
    private int timeLimit = -1;
    private long currentTimeUs = 0;
    private int muxerStatus = MUXER_STATE_UNINITIALIZED;
    private static int mCount = 0;
    private static class AVMuxerSingInstance {
        private static AudioAndVideoMuxer mAVMuxer = new AudioAndVideoMuxer();
    }
    public static AudioAndVideoMuxer getAVMuxer() {
        return AVMuxerSingInstance.mAVMuxer;
    }
    public void creatMuxer() {
        firstPTS = 0;
        currentTimeUs = 0;
        trackCount = 0;
        List<String> sd_path = loadDir();
        if (!sd_path.isEmpty()) {
            mEncoderCaptureFilename_ext = sd_path.get(0);
        } else
            mEncoderCaptureFilename_ext = "/sdcard";
        if (mEncoderCaptureFilename_ext != null) {
            Log.e(TAG, "Exist SdCard");
            File ext_dir_video = new File(mEncoderCaptureFilename_ext + "/Video/");
            if (!ext_dir_video.exists()) {
                ext_dir_video.mkdir();
            }
            mEncoderCaptureFilename = mEncoderCaptureFilename_ext + "/Video/encoder_capture" + (mCount++) + ".mp4";
            //if (mCount >= 5)
                mCount = 0;
            //mEncoderCaptureFilename = mEncoderCaptureFilename_ext;
            //mEncoderCaptureFilename_ext = "/sdcard";
        }
        try {
            mMediaMuxer = new MediaMuxer(mEncoderCaptureFilename, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            muxerStatus = MUXER_STATE_INITIALIZED;
        } catch (Exception e) {
            Log.e(TAG, "create muxer fail!");
        }
    }

    public int doMuxer(int trackIndex, ByteBuffer muxerBuffer, MediaCodec.BufferInfo bufferInfo, boolean isVideo) {
        synchronized(this) {
            if (mMediaMuxer == null) {
                if (muxerStatus == MUXER_STATE_STOPPED) {
                    Log.e(TAG, "Record video over");
                    if (trackCount > 0)
                        trackCount--;
                    if (trackCount <= 0) {
                        muxerStatus = MUXER_STATE_UNINITIALIZED;
                    }
                    return -1;
                } else if (muxerStatus == MUXER_STATE_UNINITIALIZED) {
                    Log.e(TAG, "Muxer is uninitialized!");
                    return -2;
                }
            }
            if (muxerBuffer == null) {
                Log.e(TAG, "buffer is null");
                return 0;
            }

            if (bufferInfo == null) {
                Log.e(TAG, "bufferInfo is null");
                return 0;
            }

            if ((bufferInfo.size < 0) || (bufferInfo.offset < 0) || (trackIndex < 0)) {
                Log.e(TAG, "muxer data is invalid! Index:" + trackIndex + "size: " + bufferInfo.size + "offset:" + bufferInfo.offset);
            }
            if (isVideo) {
                if (firstPTS == 0)
                    firstPTS = bufferInfo.presentationTimeUs;
                bufferInfo.presentationTimeUs -= firstPTS;
            }
            if (bufferInfo.presentationTimeUs < 0)
                bufferInfo.presentationTimeUs = 0;
            //Log.e(TAG, "doMuxer start" + trackIndex);
            if (trackCount >= 2) {
                //bufferInfo.presentationTimeUs = MediaExtractor.getSampleTime();
                mMediaMuxer.writeSampleData(trackIndex, muxerBuffer, bufferInfo);
                //Log.e(TAG, "doMuxer writeData" + trackIndex);
            } else {
                Log.e(TAG, "MediaMuxer is not start!");
                return 1;
            }
            if ((System.nanoTime() / 1000 - currentTimeUs) > (timeLimit * 60 * 1000 * 1000)) {
                stopMuxer();
                Log.e(TAG, "Record video over!");
                //return -1;
            }
        }
        return 0;
    }

    public synchronized int addTrackFormat(MediaFormat format) {
        if (mMediaMuxer == null) {
            if (muxerStatus == MUXER_STATE_STOPPED) {
                Log.e(TAG, "Record video over");
                return -1;
            } else if (muxerStatus == MUXER_STATE_UNINITIALIZED) {
                Log.e(TAG, "Muxer is uninitialized!");
                return -2;
            } else {
                Log.e(TAG, "Unknown error!");
                return -3;
            }
        }
        int index = mMediaMuxer.addTrack(format);
        Log.e(TAG, "add track" + index);
        trackCount++;
        if ((trackCount >= 2) && (muxerStatus == MUXER_STATE_INITIALIZED)) {
            mMediaMuxer.start();
            muxerStatus = MUXER_STATE_STARTED;
            Log.e(TAG,"MediaMuxer start!");
            if (currentTimeUs == 0)
                currentTimeUs = System.nanoTime()/1000;
        }
        return index;
    }

    public void stopMuxer() {
        synchronized (this) {
            if ((mMediaMuxer != null) && (muxerStatus == MUXER_STATE_STARTED)) {
                mMediaMuxer.stop();
                mMediaMuxer.release();
                mMediaMuxer = null;
                muxerStatus = MUXER_STATE_STOPPED;
            }
            Log.e(TAG, "stop muxer successful");
        }
    }

    public boolean isMuxerStart() {
        return muxerStatus == MUXER_STATE_STARTED;
    }

    public void setRecordTime(int recordTime) {
        this.timeLimit = recordTime;
    }
    private List<String> loadDir() {
        File mounts = new File("/proc/mounts");
        List<String> paths = new ArrayList<String>();

        if (mounts.exists()) {
            try {
                BufferedReader reader = new BufferedReader(
                        new FileReader(mounts));
                try {
                    String text = null;
                    while ((text = reader.readLine()) != null) {
                        Log.d(TAG, text);
                        if (text.startsWith("/dev/block/vold/")) {
                            String[] splits = text.split(" ");
                            Log.d(TAG, "len= " + splits.length);
                            if (splits.length > 2) {
                                Log.d(TAG, splits[1]);
                                paths.add(splits[1]);
                            }
                        }
                    }
                }
                finally {
                    reader.close();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Log.w(TAG, "File</proc/mounts> is not exists");
        }
        return paths;
    }
}
