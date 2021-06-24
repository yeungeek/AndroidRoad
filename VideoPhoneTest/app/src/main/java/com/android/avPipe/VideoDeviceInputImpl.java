/*
 ** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
 */
/*============================================================================
 **
 **  FILE        VideoDeviceInputImpl.java
 **
 **  PURPOSE     Implement video input and encoding using android MediaCodec
 **
 **==========================================================================*/

package com.android.avPipe;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.avPipe.AVTypes.EncoderOptimizations;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * encode
 *
 * <p>
 */
public class VideoDeviceInputImpl implements IVideoDevice, VideoCapture.VideoEncodeSync {

    private static final String TAG = "VideoDeviceInputImpl";
    private final int MAX_ENCODED_FRAMES = 10;
    private static final int ENCODER_QUEUE_DEPTH = 1;
    private boolean isVideoMute = false;
    private boolean isRunning = true;
    private boolean isEncoderOutputStarted = false;
    private boolean isEncoderInputStarted = false;
    private MediaCodec mEncodeMediaCodec;
    private ByteBuffer[] mEncodeInputBuffer;
    private ByteBuffer[] mEncodeOutputBuffer;
    private BlockingQueue<ByteBuffer> UnencodedFramesQueue = new ArrayBlockingQueue<ByteBuffer>(2);
    private BlockingQueue<Integer> EncodedFrameIndexes = new ArrayBlockingQueue<Integer>(MAX_ENCODED_FRAMES);
    private BlockingQueue<MediaCodec.BufferInfo> EncodedFrameInfos = new ArrayBlockingQueue<MediaCodec.BufferInfo>(MAX_ENCODED_FRAMES);
    private ByteBuffer mSPS_PPS_Buffer;
    private SurfaceView mShowVideoView;
    private VideoFormatInfo mVideoFormatInfo;
    private Thread mEncoderInputThread;
    private Thread mEncoderOutputThread;
    private FileOutputStream fos;
    private EncodeSettings mEncodeSettings;
    private IVideoDeviceCallback mVideoDeviceCb;
    private int mEncoderBitrateKbps = 4000;
    private boolean mSoftEncode = false;
    private EncodedFrameListener mEncodedFrameListener;
    private EncoderOptimizations mEncoderOptimizations = AVTypes.getEncoderOptsInstance();
    private AudioAndVideoMuxer avMuxer = AudioAndVideoMuxer.getAVMuxer();
    private String video_hevc="video/hevc";
    private int count = 0;
    private long firstFramePts = 0;
    private MediaFormat format = null;
    public MediaMuxer mMuxer;
    /* debug */
    private static final boolean PROFILE_VIDEO = false;
    private static final boolean DEBUG = false;
    private int result = 0;
    private final String capture_filename = "/sdcard/capture.h264";
    private boolean hasSyncFrame = false;
    private boolean hasNoIDRFrame = false;
   // private boolean isCapture = false;
    protected int mTrackIndex;
    private long preTimeStamp = 0;
    private long timeInterval = 0;
    private int countFrame = 0;
    public interface EncodedFrameListener {
        public void onEncodedFrame(MediaCodec.BufferInfo bufferInfo);
    }

    public void setEncodedFrameListener(EncodedFrameListener l) {
        mEncodedFrameListener = l;
    }

    public EncoderOptimizations getEncoderOptimizations() {
        return mEncoderOptimizations;
    }
    public void setEncoderOptimizations(EncoderOptimizations opts) {
        mEncoderOptimizations = opts;
    }

    public VideoDeviceInputImpl() {
        // ideally we wouldn't do any creation in a ctor but open is not called until
        // a call is started and that is too late to set defaults.  Consider moving this.
        mEncodeSettings = new EncodeSettings();
        mEncodeSettings.setDefaults(mEncoderOptimizations);
    }

    public void setShowView(SurfaceView showVideoView) {
        this.mShowVideoView = showVideoView;
    }

    /**
     * update preview position
     *
     * @param info
     */
    public void updatePreviewInfo(VideoPreviewInfo info) {

    }

    public void setCallback(IVideoDeviceCallback cb) {
        mVideoDeviceCb = cb;
    }

    @Override
    public void open(VideoFormatInfo vfi, boolean useSoftCodec) {
        mVideoFormatInfo = vfi;
        mSoftEncode = useSoftCodec;

        if (DEBUG) {
            Log.d(TAG, "open");
            try {
                fos = new FileOutputStream(capture_filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        mVideoFormatInfo.setWidth((mVideoFormatInfo.getWidth() + 15) / 16 * 16);
        mVideoFormatInfo.setHeight(/*(*/mVideoFormatInfo.getHeight() /*+ 15) / 16 * 16*/);
        //mVideoFormatInfo.setBitRate(mVideoFormatInfo.getBitRate());

        if (VideoCapture.Instance().isStarted()) {
            VideoCapture.Instance().stop();
        }
        VideoCapture.Instance().openCamera(true);
        VideoCapture.Instance().setEncodeSize(mVideoFormatInfo.getWidth(), mVideoFormatInfo.getHeight());
        VideoCapture.Instance().setTextureView(mShowVideoView);
        VideoCapture.Instance().setEncoderCallback(this);
		Log.d(TAG, "open");
        initEncodeMediaCodec();
    }

    @Override
    public void setVideoFormat(VideoFormatInfo vfi) { }

    @Override
    public void setVideoMute(boolean Mute) { this.isVideoMute = Mute; }

    @Override
    public void start() {
        //if (DEBUG)
            Log.d(TAG, "start");

        Log.i(TAG, "mEncodeMediaCodec.start()");
        mEncodeMediaCodec.start();
        mEncodeInputBuffer = mEncodeMediaCodec.getInputBuffers();
        mEncodeOutputBuffer = mEncodeMediaCodec.getOutputBuffers();

        isRunning = true;

        /* if the bitrate was adjusted between call start and encoder start time adjust it now
         * to maintain mocha settings. */
/*        if (mVideoFormatInfo.getBitRate() != (mEncoderBitrateKbps * 1000)) {
            setBitrate(mEncoderBitrateKbps);
        }
*/
        feedCameraFramesToEncoder();
        gatherEncodedFrames();

        VideoCapture.Instance().startCapture();
    }

    @Override
    public void stop() {
        if (DEBUG) {
            Log.i(TAG, "stop");
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        isRunning = false;
        VideoCapture.Instance().stop();
        // interrupt the camera->encoder thread
        if (mEncoderInputThread != null && !mEncoderInputThread.isInterrupted() && mEncoderInputThread.isAlive()) {
            try {
                mEncoderInputThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // interrupt encoder output thread
        if (mEncoderOutputThread != null && !mEncoderOutputThread.isInterrupted() && mEncoderOutputThread.isAlive()) {
            try {
                mEncoderOutputThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mEncodeMediaCodec != null) {
            //while (true) {
            //    if (isEncoderInputStarted || isEncoderOutputStarted) {
            //        continue;
            //    }

                releaseEncodedFrames();
                Log.i(TAG, "mEncodeMediaCodec.stop()");
                mEncodeMediaCodec.stop();
            //    break;
            //}
        }
    }

    @Override
    public void close() {
        //if (DEBUG) {
            Log.i(TAG, "close");
        //}
        isRunning = false;
        if (mEncodeMediaCodec != null) {
            Log.i(TAG, "mEncodeMediaCodec.release()");
            mEncodeMediaCodec.release();
            mEncodeMediaCodec = null;
            mEncodeSettings.setEncoder(null);
        }
        mSPS_PPS_Buffer = null;
        UnencodedFramesQueue.clear();
    }

    @Override
    public void abort() { }

    @Override
    public int getDevNum() { return 0; }

    @Override
    public int getCurrentDev() { return 0; }

    @Override
    public DevInfo getDevInfo(int devId) { return null; }

    //public View getShowView() { return mLinearLayout; }

    /**
     * init encode mediacodec
     */
    private void initEncodeMediaCodec() {
        try {
			Log.d(TAG,"initEncodeMediaCodec mSoftEncode:"+mSoftEncode);
            if (mSoftEncode) {
                mEncodeMediaCodec = MediaCodec.createByCodecName("OMX.google.h264.encoder");
            } else {
                mEncodeMediaCodec = MediaCodec.createEncoderByType(mVideoFormatInfo.getMimeType());
            }

            MediaFormat format = MediaFormat.createVideoFormat(mVideoFormatInfo.getMimeType(), mVideoFormatInfo.getWidth(), mVideoFormatInfo.getHeight());

            /*set framerate*/
            if (mEncoderOptimizations.getEncoder_framerate() > 0) {
                //mVideoFormatInfo.setFrameRate(mEncoderOptimizations.encoder_framerate_value);
                //Log.e("TAG", "encoder mEncoderOptimizations framerate = " + mEncoderOptimizations.encoder_framerate_value);
                VideoCapture.Instance().setFrameRate(mVideoFormatInfo.getFrameRate());
            }
            format.setInteger(MediaFormat.KEY_FRAME_RATE, mVideoFormatInfo.getFrameRate());

            /*i frame interval*/
            //if (mEncoderOptimizations.encoder_iframe_interval_enable && mEncoderOptimizations.encoder_iframe_interval != -1)
                //mVideoFormatInfo.setIframeInterval(mEncoderOptimizations.encoder_iframe_interval);
            Log.e("TAG","i frame interval is " + mVideoFormatInfo.getIframeInterval());
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, mVideoFormatInfo.getIframeInterval());
            //format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, -1);
            /*encoder bitrate*/
            if (/*mEncoderOptimizations.encoder_bitrate_enable && */mEncoderOptimizations.getEncoder_bitrate() != 0) {
                Log.e(TAG, "Set Bitrate as " + (mEncoderOptimizations.getEncoder_bitrate()*1000));
                //mVideoFormatInfo.setBitRate(mEncoderOptimizations.encoder_bitrate_value*1000);
                mEncoderBitrateKbps = mEncoderOptimizations.getEncoder_bitrate();
                mEncodeSettings.setBitrate(mEncoderOptimizations.getEncoder_bitrate());
            }
            format.setInteger(MediaFormat.KEY_BIT_RATE, mVideoFormatInfo.getBitRate());
            //if (mEncoderOptimizations.encoder_timeLimit_enable && mEncoderOptimizations.encoder_timeLimit_value != 0)
               // timeLimit = mEncoderOptimizations.encoder_timeLimit_value;
            /*color format*/
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion > 23)
                mVideoFormatInfo.setEncodeColorFormat(MediaCodecInfo.CodecCapabilities.COLOR_QCOM_FormatYUV420SemiPlanar);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, mVideoFormatInfo.getEncodeColorFormat());

            // Tell the EncodeSettings class what MediaCodec to configure.
            mEncodeSettings.clearSession();
            mEncodeSettings.setEncoder(mEncodeMediaCodec);

/*            if(true) {
                format.setInteger(MediaFormat.KEY_EXTENDED_FLAG, 1);
                format.setInteger(MediaFormat.KEY_EXTENDED_SCAL_WIDTH, 1280);
                format.setInteger(MediaFormat.KEY_EXTENDED_SCAL_HEIGHT, 720);
                format.setInteger(MediaFormat.KEY_EXTENDED_CROP_LEFT, 0);
                format.setInteger(MediaFormat.KEY_EXTENDED_CROP_RIGHT, 0);
                format.setInteger(MediaFormat.KEY_EXTENDED_CROP_TOP, 0);
                format.setInteger(MediaFormat.KEY_EXTENDED_CROP_BOTTOM, 0);
            }
*/
            Log.d(TAG,"initEncodeMediaCodec format:"+format.toString());
            mEncodeMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

        } catch (Exception e) {
            e.printStackTrace();
			Log.d(TAG,"initEncodeMediaCodec err:"+e.getMessage());
        }
    }

    @Override
    public void onVideoCaptureStarted() {
        Log.d(TAG,"onVideoCaptureStarted");
    }

    @Override
    public void onVideoCaptureFrame(byte[] data, long timestamp) {
        if (data == null)
            return;

        if (PROFILE_VIDEO) {
            Log.i("VideoProfile", "    Camera capture, Camera Timestamp = " + (timestamp / 1000000)
                    + ", System time = " + (System.nanoTime() / 1000000));
        }

        if (mVideoDeviceCb != null) {
            mVideoDeviceCb.onFrameRecv(timestamp);
        }
        if (frameNeedEncoder(timestamp)) {
            UnencodedFramesQueue.offer(ByteBuffer.wrap(data));
            //Log.e(TAG, "DEUBG: camera get data:" + System.nanoTime());
        }
    }

    /**
     * Generates the presentation time for frame N, in microseconds.
     */
    private long computePresentationTime(int frameIndex) {
        return 132 + (long)frameIndex * 1000000 / mVideoFormatInfo.getFrameRate();
    }

    /**
     * start feeding caputured camera frames to the encoder (MediaCodec)
     */
    private void feedCameraFramesToEncoder() {
        mEncoderInputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                isEncoderInputStarted = true;
                int generateIndex = 0;
                int width = mVideoFormatInfo.getWidth();
                int height = mVideoFormatInfo.getHeight();
                int widthTimesHeight = width * height;
                int wh4 = widthTimesHeight / 4;
                final ByteBuffer blackFramebuffer = ByteBuffer.allocate(widthTimesHeight * 3 / 2);
                Arrays.fill(blackFramebuffer.array(), 0, widthTimesHeight, (byte) 0);
                Arrays.fill(blackFramebuffer.array(), widthTimesHeight, blackFramebuffer.capacity(), (byte) 128);

                while (isRunning) {
                    ByteBuffer UnencodedFrame = null;
                    long ptsUsec = 0;
                    while (UnencodedFrame == null && isRunning) {
                        try {
                            UnencodedFrame = UnencodedFramesQueue.poll(50, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.e(TAG, "UnencodedFramesQueue exception: " + e.getMessage());
                        }
                    }

                    if (UnencodedFrame == null) {
                        continue;
                    }

                    if (!isRunning)
                        break;

                    int index = -100;
                    int WaitMicroSeconds = 0;
                    while (index < 0 && isRunning) {
                        try {
                            index = mEncodeMediaCodec.dequeueInputBuffer(WaitMicroSeconds);
                            if ((index < 0) || (index >= ENCODER_QUEUE_DEPTH)) {
                                Thread.sleep(5);
                                if (index >= ENCODER_QUEUE_DEPTH)
                                    index = -100;
								Log.e(TAG, "index <>:" + index);
                                continue;
                            }
                            //Log.e(TAG, "DEUBG: encoder dequeueInput buffer:" + System.nanoTime());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            Log.e(TAG, "exception: " + e1.getMessage());
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }
                    }
					
					if (!isRunning)
                        break;

                    /* We have a frame and we are going to fill an input buffer on the media codec below this line */
                    mEncodeInputBuffer[index].clear();

                    if (isVideoMute) {
                        mEncodeInputBuffer[index].put(blackFramebuffer.array());
                    } else {
                        mEncodeInputBuffer[index].put(UnencodedFrame.array());
                    }
                    /*if (firstFramePts == 0) {
                        firstFramePts = System.nanoTime() / 1000;
                        ptsUsec = 0;
                        encoderFrameCount++;
                    } else {
                        long ptsInterval = getTimeInterval() / 1000;
                        if (((System.nanoTime() / 1000 - firstFramePts) / ptsInterval) > encoderFrameCount) {
                            encoderFrameCount = (System.nanoTime() / 1000 - firstFramePts) / ptsInterval;
                        } else {
                            encoderFrameCount++;
                        }
                        ptsUsec = encoderFrameCount * ptsInterval;
                    }*/
                    if (firstFramePts == 0) {
                        firstFramePts = System.nanoTime() / 1000;
                        ptsUsec = 132;
                    } else
                        ptsUsec = System.nanoTime() / 1000 - firstFramePts + 132;
                    //ptsUsec = computePresentationTime(generateIndex++);
                    //if (currentTimeUs == 0)
                    //    currentTimeUs = System.nanoTime() / 1000;
                    //ptsUsec = System.nanoTime() / 1000;// - currentTimeUs;
                    if (PROFILE_VIDEO) {
                        Log.i("VideoProfile", "    Encoder input, Encoder Timestamp = " + (ptsUsec / 1000)
                                + ", System time = " + (System.nanoTime() / 1000000));
                    }

                    if (mVideoDeviceCb != null) {
                        mVideoDeviceCb.onFrameIn(ptsUsec);
                    }

                    try {
                        mEncodeMediaCodec.queueInputBuffer(index, 0, UnencodedFrame.capacity(), ptsUsec, 0);
                        //Log.e(TAG, "DEUBG: encoder Input buffer" + System.nanoTime());
                    } catch (Exception e) {
                        Log.e(TAG, "mEncodeMediaCodec.queueInputBuffer:" + e.getMessage());
                    }
                    if (mEncoderOptimizations.getEncoder_bitrate() != 0) {
                        mEncoderBitrateKbps = mEncoderOptimizations.getEncoder_bitrate();
                        if (mVideoFormatInfo.getBitRate() != (mEncoderBitrateKbps * 1000)) {
                            setBitrate(mEncoderBitrateKbps);
                            mVideoFormatInfo.setBitRate(mEncoderBitrateKbps * 1000);
                        }
                    }
                    if (mEncoderOptimizations.encoder_request_idr) {
                        requestIDRFrame();
                        mEncoderOptimizations.encoder_request_idr = false;
                    }
                }
                isEncoderInputStarted = false;
            }
        });
        mEncoderInputThread.setName("video.encoder.input");
        mEncoderInputThread.start();
    }

    private void gatherEncodedFrames() {
        mEncoderOutputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().setName("EncodedFrames");
                } catch (Exception e){
                    Log.e(TAG, e.getMessage());
                }
                isEncoderOutputStarted = true;
                int generateIndex = 0;
                while (isRunning) {
                    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    int index = -100;
                    int WaitMicroSeconds = 0;
                    //long tmp = System.currentTimeMillis();
                    try {
                        //Log.e(TAG, "encodeOutput cost time:" + (System.currentTimeMillis() - tmp) + "index:" + index);
                        index = mEncodeMediaCodec.dequeueOutputBuffer(bufferInfo, WaitMicroSeconds);
                        if (bufferInfo.flags == MediaCodec.BUFFER_FLAG_SYNC_FRAME)
                            hasNoIDRFrame = false;
                        /*if (bufferInfo.flags == MediaCodec.BUFFER_FLAG_SYNC_FRAME)
                            Log.e(TAG, "sync frame" + index);
                        else if(bufferInfo.flags == MediaCodec.BUFFER_FLAG_CODEC_CONFIG)
                            Log.e(TAG, "codec config");
                        else if (bufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                            Log.e(TAG, "end flag");
                        else
                            Log.e(TAG, "Unknown flag");*/
                        //Log.e(TAG, "DEUBG: encoder output buffer" + System.nanoTime());
                       // Log.e(TAG, "dequeueOutputBuffer");
                    } catch (Exception e) {
						e.printStackTrace();
                        Log.e(TAG, "dequeueOutputBuffer error " + e.getMessage());
                    }

                    if (index == MediaCodec.INFO_TRY_AGAIN_LATER) {
                        try {
                            Thread.sleep(20);
                            Log.e(TAG, "MediaCodec.INFO_TRY_AGAIN_LATER");
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    } else if (index == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                        mEncodeOutputBuffer = mEncodeMediaCodec.getOutputBuffers();
						Log.e(TAG, "MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED");
                    } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                        format = mEncodeMediaCodec.getOutputFormat();
                        //if (isCapture) {
                            mTrackIndex = avMuxer.addTrackFormat(format);
                            Log.e(TAG, "INFO_OUTPUT_FORMAT_CHANGED");
                            /*mTrackIndex = mMuxer.addTrack(format);
                            if (!mMuxerStarted) {
                                mMuxer.start();
                                mMuxerStarted = true;
                            }*/
                        //}
                        //if (DEBUG)
                            Log.i(TAG, "media format changed " + format);
                    } else if (index >= 0) {
                        if (PROFILE_VIDEO) {
                            Log.i("VideoProfile", "    Encoder output, Encoder Timestamp = " + (bufferInfo.presentationTimeUs / 1000)
                                    + ", System time = " + (System.nanoTime() / 1000000));
                        }
                        if (mVideoDeviceCb != null) {
                            mVideoDeviceCb.onFrameOut(bufferInfo.presentationTimeUs, bufferInfo.size);
                        }
                        //if (isCapture && !mMuxerStarted) {
                        // muxer is not ready...this will prrograming failure.
                        // throw new RuntimeException("gatherEncodedFrames: muxer hasn't started");
                        //}
                        if ((mTrackIndex >= 0) && (result >= 0)) {
                            if (!hasSyncFrame) {
                                if (avMuxer.isMuxerStart()) {
                                    if (bufferInfo.flags == MediaCodec.BUFFER_FLAG_SYNC_FRAME) {
                                        hasSyncFrame = true;
                                        count = 0;
                                    } else {
                                        count = mVideoFormatInfo.getIframeInterval();
                                    }
                                }
                            }
                            final ByteBuffer encodedData = mEncodeOutputBuffer[index];
                            if (hasSyncFrame)
                                result = avMuxer.doMuxer(mTrackIndex, encodedData, bufferInfo, true);
                            //Log.e(TAG, "mEncodeMediaCodec doMuxer: " + mTrackIndex);
                            if (result == -1)
                                Log.e(TAG, "record over");
                            else if (result == -2)
                                Log.e(TAG, "do not need muxer");
                            /*else if (result == 1) {
                                count = mVideoFormatInfo.getIframeInterval();
                                hasSyncFrame = false;
                                Log.e(TAG, "muxer is not start!");
                            }*/
                        }
                        bufferInfo.presentationTimeUs = computePresentationTime(generateIndex++);
                        queueEncodedFrame(index, bufferInfo);
                        if (mVideoFormatInfo.getIframeInterval() != -1) {
                            if (count < mVideoFormatInfo.getIframeInterval())
                                count++;
                            else {
                                count = 0;
                                requestIDRFrame();
                            }
                        }
                    } else {
                        //Log.e(TAG, "DEBUG: encoder output buffer index:" + index + "time: " + System.nanoTime());
                    }
                }
                isEncoderOutputStarted = false;
            }
        });
        mEncoderOutputThread.setName("video.encoder.output");
        mEncoderOutputThread.start();
    }

    private void writeEncodedFrameDebug(ByteBuffer frame, int size) {
        if (DEBUG) {
            try {
                if (frame.isDirect()) {
                    byte[] out = new byte[size];
                    frame.get(out);
                    fos.write(out);
                } else {
                    fos.write(frame.array());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printSPSPPS(ByteBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buffer.array())
            sb.append(String.format("%02x ", b));
        Log.v(TAG, "SPS-PPS : " + sb.toString());
    }

    private void queueEncodedFrame(int index, MediaCodec.BufferInfo bufferInfo) {
        synchronized(this) {
            EncodedFrameIndexes.add(index);
            EncodedFrameInfos.add(bufferInfo);
            if (mEncodedFrameListener != null) {
                mEncodedFrameListener.onEncodedFrame(bufferInfo);
            }
        }
    }

    private void copyEncodedFrame(ByteBuffer dest, int index, MediaCodec.BufferInfo bufferInfo) {

        /* setup position we read from and limit of reads on the mediacodec buffer. */
        mEncodeOutputBuffer[index].position(bufferInfo.offset);
        mEncodeOutputBuffer[index].limit(bufferInfo.offset + bufferInfo.size);

        dest.clear();
        dest.limit(bufferInfo.size);

        /* if the encoder is inserting SPSPPS packets for us, just copy and return. */
        if (mEncodeSettings != null && mEncodeSettings.isSPSPPSEnabled()) {
            dest.put(mEncodeOutputBuffer[index]);
            mEncodeMediaCodec.releaseOutputBuffer(index, false);
            return;
        }

        /* if we have not caught our first sps-pps buffer, queue it and return */
        if (mSPS_PPS_Buffer == null) {
            // Log.v(TAG, "--insert first SPS_PPS packet");
            mSPS_PPS_Buffer = ByteBuffer.allocate(bufferInfo.size);
            mSPS_PPS_Buffer.put(mEncodeOutputBuffer[index]);
            if (mVideoFormatInfo.getMimeType().equals(video_hevc))
                mSPS_PPS_Buffer.flip();
            dest.put(mSPS_PPS_Buffer);
            mEncodeMediaCodec.releaseOutputBuffer(index, false);
            return;
        }

        /* we may need to prepend sps-pps data to the current encoded frame. */
        if ((mEncodeOutputBuffer[index].get(4) & 0x0F) == 5) {
            // Log.v(TAG, "--drop in another SPS_PPS packet");
            dest.limit(mEncodeOutputBuffer[index].limit() + mSPS_PPS_Buffer.capacity());
            dest.put(mSPS_PPS_Buffer.array());
            dest.put(mEncodeOutputBuffer[index]);
            mEncodeMediaCodec.releaseOutputBuffer(index, false);
            return;
        }

        // default, copy frame onward.
        dest.put(mEncodeOutputBuffer[index]);
        mEncodeMediaCodec.releaseOutputBuffer(index, false);
    }

    private void releaseEncodedFrames() {
        MediaCodec.BufferInfo info;
        Integer index;
        for (int i=0; i<MAX_ENCODED_FRAMES; ++i) {
            index = EncodedFrameIndexes.poll();
            info = EncodedFrameInfos.poll();
            if ((index != null) && (info != null)) {
                Log.v(TAG, "releaseEncodedFrames: release index " + index + " buffer " + info);
                mEncodeMediaCodec.releaseOutputBuffer(index, false);
            }
        }
    }
    public int setRequestIdr(boolean request_idr) {
        mEncoderOptimizations.encoder_request_idr = request_idr;
        return 0;
    }
    /**
     * called by jni to pull encoded frames out of the encoder to send over the network.
     *
     * @param request
     * @return
     */
    public int read(ReadRequest request) {
        if (!isRunning)
            return 0;

        /* synch so that codec stop/starts won't affect a read mid state change. */
        synchronized(this) {
            MediaCodec.BufferInfo info = EncodedFrameInfos.poll();
            Integer index = EncodedFrameIndexes.poll();
            if ((info != null) && (index != null)) {
                if (mVideoDeviceCb != null) {
                    mVideoDeviceCb.onFrameConsumed(info.presentationTimeUs);
                }

                request.getBuffer().rewind();
                copyEncodedFrame(request.getBuffer(), index, info);
                return request.getBuffer().position();
            }
        }
        return 0;
    }

    public void setMediaMuxer(MediaMuxer muxer) {
        mMuxer = muxer;
    }

    //public void setCaptureFlag(boolean flag) {
    //    isCapture = flag;
    //}

    /* used for testing */
    public MediaCodec.BufferInfo peek() {
        return EncodedFrameInfos.peek();
    }

    /**
     * Called from JNI when the encoder has been requested to generate a new IDR frame for the Decoder
     * across the connection.
     */
    private void requestIDRFrame() {
        if ((mEncodeSettings != null) && (!hasNoIDRFrame)) {
            hasNoIDRFrame = true;
            mEncodeSettings.requestIDRFrame();
        }
    }

    /**
     * Called from JNI when the encoder has been requested a bitrate change to the encoder.
     */
    private void setBitrate(int bitrate) {
        mEncoderBitrateKbps = bitrate;
        if (isRunning && (mEncodeSettings != null)) {
            mEncodeSettings.setBitrate(mEncoderBitrateKbps);
        }
    }
    private boolean frameNeedEncoder(long timestamp) {
        if (mEncoderOptimizations.getEncoder_framerate() == 30)
            return true;
        timeInterval = getTimeInterval();
        if (preTimeStamp == 0) {
            countFrame++;
            preTimeStamp = timestamp;
            return true;
        } else if ((preTimeStamp + timeInterval) <= (timestamp - 5000000)) {
            if (countFrame < 1024) {
                preTimeStamp += timeInterval;
                countFrame++;
            } else {
                preTimeStamp = timestamp;
                countFrame = 0;
            }
            return true;
        } else
            return false;
    }
    private long getTimeInterval() {
        return (long)1000000000/(mEncoderOptimizations.getEncoder_framerate());
    }
    public void setResult(int result) {
        this.result = result;
        if (format != null)
           mTrackIndex = avMuxer.addTrackFormat(format);
        hasSyncFrame = false;
    }
}
