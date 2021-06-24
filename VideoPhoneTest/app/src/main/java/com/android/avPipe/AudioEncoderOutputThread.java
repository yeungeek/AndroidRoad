package com.android.avPipe;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by Renjiang.Han on 2018/7/5.
 */
public class AudioEncoderOutputThread extends Thread {
    private static final String TAG = "AudioEncoderOutput";
    private MediaCodec mAudioEncoder;
    private ByteBuffer[] mAudioEncodeOutputBuffer;
    private AudioAndVideoMuxer avMuxer = AudioAndVideoMuxer.getAVMuxer();
    private boolean isRecording = false;
    private int mTrackIndex;
    private int result = 0;
    public AudioEncoderOutputThread(MediaCodec encoder) {
        this.mAudioEncoder = encoder;
    }
    public int getResult() {
        return result;
    }
    public boolean isRecording() {
        return isRecording;
    }
    public void setRecording(boolean isRecording) {
        Log.e(TAG, "setRecording is " + isRecording);
        this.isRecording = isRecording;
    }
    public MediaCodec getAudioEncoder() {
        return mAudioEncoder;
    }
    public void setAudioEncoder(MediaCodec mEncoder) {
        this.mAudioEncoder = mEncoder;
    }
    @Override
    public void run() {
        Log.e(TAG, "AudioEncoderOutputThread run!");
        mAudioEncodeOutputBuffer = mAudioEncoder.getOutputBuffers();
        int index = 0;
        int timeoutUs = 10000;
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        while (isRecording()) {
            try {
                index = mAudioEncoder.dequeueOutputBuffer(bufferInfo, timeoutUs);
                Log.e(TAG,"mAudioEncoder.dequeueOutputBuffer:" + index);
                if (index == MediaCodec.INFO_TRY_AGAIN_LATER) {
                } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    MediaFormat format = mAudioEncoder.getOutputFormat();
                    mTrackIndex = avMuxer.addTrackFormat(format);
                    Log.e(TAG,"addTrackFormat:" + mTrackIndex);
                } else if (index == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    mAudioEncodeOutputBuffer = mAudioEncoder.getOutputBuffers();
                } else if(index >= 0) {
                    if ((mTrackIndex >= 0) && (result >= 0)) {
                        final ByteBuffer encodedData = mAudioEncodeOutputBuffer[index];
                        result = avMuxer.doMuxer(mTrackIndex, encodedData, bufferInfo, false);
                        if (result == -1)
                            Log.e(TAG, "record over");
                        else if (result == -2)
                            Log.e(TAG, "do not need muxer");
                    }
                    mAudioEncoder.releaseOutputBuffer(index, false);
                    Log.e(TAG,"mAudioEncoder doMuxer: mTrackIndex: " + mTrackIndex + "result: " + result);
                }
            } catch (Exception e) {
                Log.e(TAG, "dequeueOutputBuffer error " + e.getMessage());
            }
        }
    }
}
