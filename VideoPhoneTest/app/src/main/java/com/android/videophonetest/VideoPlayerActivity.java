package com.android.videophonetest;

/**
 * Created by Renjiang.Han on 2018/7/25.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.UserManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.io.IOException;

import com.android.avPipe.AVTypes;
import com.android.avPipe.AudioAndVideoMuxer;
import com.android.avPipe.IVideoDevice;
import com.android.avPipe.LocalAudioLoopThread;

import com.android.avPipe.ReadRequest;
import com.android.avPipe.VideoCapture;
import com.android.avPipe.VideoDeviceInputImpl;
import com.android.avPipe.VideoDeviceOutputImpl;
import com.android.avPipe.VideoFormatInfo;

public class VideoPlayerActivity extends Activity implements
        VideoDeviceInputImpl.EncodedFrameListener
{
    private static final String TAG = "VideoPlayerActivity";
    private static final int BUFFER_SIZE = 512 * 1024;
    private static int FRAME_THROUGHPUT_INTERVAL = 60;
    private static int FRAME_Rate = 0;
    private static boolean USE_SW_ENCODER = false;
    private static final boolean ENCODER_ONLY = false;
    private VideoFormatInfo mVideoFormatInfo;
    private VideoDeviceInputImpl mVideoInput;
    private VideoDeviceOutputImpl mVideoOutput;
    private ByteBuffer mVideoBuffer;
    private boolean mIsStarted = false;
    private AlertDialog mVideoResDialog = null;

    private Button mEncoderCaptureButton;
    private boolean mEncoderCapturing = false;

    private SurfaceView mPreviewSfc;
    private SurfaceView mDecodeSfc;
    private LinearLayout mStatsFrame;
    private TextView mTimerDisplay;
    private LocalAudioLoopThread mAudioLoop;
    private Thread audioRecoderInput = null;
    public static ConditionVariable sCv = new ConditionVariable();
    private String video_avc = "video/avc";
    private String video_hevc = "video/hevc";
    private int timeLimit = -1;
    private long mCountdown = 0;
    private CameraIsExist mCameraIsExist;
    private boolean isCameraExist = false;
    private AVTypes.EncoderOptimizations opts = AVTypes.getEncoderOptsInstance();
    private AudioAndVideoMuxer avMuxer = AudioAndVideoMuxer.getAVMuxer();
    private boolean isRunnable = false;
    File ext_dir = new File("/storage/external_storage/sda1/");
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);

        mVideoFormatInfo = new VideoFormatInfo();

        mVideoInput = new VideoDeviceInputImpl();
        mVideoInput.setCallback(new VideoCallback(true));
        if (!ENCODER_ONLY)
        {
            mVideoOutput = new VideoDeviceOutputImpl();
            mVideoOutput.setCallback(new VideoCallback(false));
        }
        mVideoBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        // either or, one of these next routines will feed the decoder.
        mVideoInput.setEncodedFrameListener(this);
        mAudioLoop = new LocalAudioLoopThread();
        loadResources();
        mCameraIsExist = new CameraIsExist();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mCameraIsExist, filter);
        sCv.close();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.e(TAG, "onResume end!");
        //updateStats();
    }

    @Override
    protected void onPause()
    {
        if (mIsStarted)
        {
            stopVideo();
            VideoCapture.Instance().closeCamera();
        }
        if (mVideoResDialog != null)
        {
            mVideoResDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
		if (mCameraIsExist != null) {
            unregisterReceiver(mCameraIsExist);
            mCameraIsExist = null;
        }
        VideoCapture.Instance().closeCamera();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub
        
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Log.d(TAG, "KEYCODE_BACK");
            mAudioLoop.setRecording(false);
            avMuxer.stopMuxer();
            stopVideo();
            while (audioRecoderInput != null) {
                if (!audioRecoderInput.isAlive())
                    break;
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    Log.e(TAG, "audioRecod thread wait" + e.getMessage());
                }
				Log.e(TAG,"wait audioRecoderInput not alive!!!");
            }
            if (isRunnable == true) {
                isRunnable = false;
                handler.removeCallbacks(runnable);
                mTimerDisplay.setVisibility(View.GONE);
            }
            isCameraExist = false;
            runnable = null;
            handler = null;
            audioRecoderInput = null;
            timeLimit = -1;
            VideoCapture.Instance().closeCamera();
            Intent intent = new Intent(VideoPlayerActivity.this, MainActivity.class);
            startActivity(intent);
			Log.d(TAG, "finish");
            finish();
            // 这里不需要执行父类的点击事件，所以直接return
            return true;
        }
        // 继续执行父类的其他点击事件
        return super.onKeyDown(keyCode, event);
    }

    public VideoDeviceInputImpl getVideoInputDevice()
    {
        return mVideoInput;
    }

    private void loadResources()
    {
        mStatsFrame = (LinearLayout) findViewById(R.id.frameStats);
        mTimerDisplay = (TextView) findViewById(R.id.timeDisplay);
        mTimerDisplay.setTextColor(Color.RED);
        mDecodeSfc = (SurfaceView) findViewById(R.id.videoOutput);
        mPreviewSfc = (SurfaceView) findViewById(R.id.videoInput);
        mDecodeSfc.setVisibility(View.VISIBLE);
        mPreviewSfc.setVisibility(View.VISIBLE);
		mPreviewSfc.setZOrderOnTop(true);
        mEncoderCaptureButton = (Button) findViewById(R.id.btnEncoderCapture);
        mEncoderCaptureButton.setText("录制");
        mEncoderCaptureButton.requestFocus();
        /*set type*/
        if (opts.getEncodertype().equals("H.264")) {
            mVideoFormatInfo.setMimeType(video_avc);
            Log.e(TAG, "video mime:" + opts.getEncodertype());
        } else if (opts.getEncodertype().equals("H.265")) {
            mVideoFormatInfo.setMimeType(video_hevc);
            Log.e(TAG, "video mime:" + opts.getEncodertype());
        }
        /*encoder bitrate*/
        if (opts.getEncoder_bitrate() != 0) {
            mVideoFormatInfo.setBitRate(opts.getEncoder_bitrate() * 1000);
            Log.e(TAG, "BitRate: " + opts.getEncoder_bitrate() * 1000);
        }

        /*set Resolution*/
        int[] resolution = opts.getResolution();
        if (resolution[0] > 0) {
            mVideoFormatInfo.setWidth(resolution[0]);
            mVideoFormatInfo.setHeight(resolution[1]);
            Log.e(TAG, "Resolution:" + resolution[0] + "x" + resolution[1]);
        }
        /*set framerate*/
        if (opts.getEncoder_framerate() > 0) {
            mVideoFormatInfo.setFrameRate(opts.getEncoder_framerate());
            FRAME_Rate = opts.getEncoder_framerate();
            Log.e("TAG", "encoder mEncoderOptimizations framerate = " + opts.getEncoder_framerate());
        }
        /*i frame interval*/
        if (opts.getEncoder_iframe() >= 0) {
            mVideoFormatInfo.setIframeInterval(opts.getEncoder_iframe());
            Log.e("TAG", "i frame interval is " + mVideoFormatInfo.getIframeInterval());
        }
        /*encoder Record time*/
        if (opts.getEncoder_timeLimit() > 0) {
            timeLimit = opts.getEncoder_timeLimit();
            Log.e(TAG, "Record time " + timeLimit);
        }
        mVideoInput.setEncoderOptimizations(opts);
        showResolutionOptions_iptv();
    }

    public void onEncodedFrame(MediaCodec.BufferInfo info)
    {
        if (mVideoInput.read(new ReadRequest(mVideoBuffer)) > 0)
        {
            mVideoBuffer.rewind();
            if(mVideoOutput.write(mVideoBuffer, info.presentationTimeUs,
                    ((info.flags & MediaCodec.BUFFER_FLAG_SYNC_FRAME) != 0)) == 1) {
                mVideoInput.setRequestIdr(true);
                Log.e(TAG, "debug: request idr");
            }
        }
    }

    private void showResolutionOptions_iptv()
    {
		Log.e(TAG, "showResolutionOptions_iptv");
        String text = "请检查是否有camera插入";
        if (VideoCapture.Instance().openCamera(true) == false) {
            Toast.makeText(getApplicationContext(), text,
                    Toast.LENGTH_SHORT).show();
			Log.e(TAG, "isCameraExist false");
            return;
        }
        isCameraExist = true;
        final AVTypes.VideoFmt[] fmts = VideoCapture.Instance().getSupportedFormats();
        int index = 0;
        for (index = 0; index < fmts.length; ++index) {
            if (fmts[index].width == mVideoFormatInfo.getWidth()
                    || fmts[index].height == mVideoFormatInfo.getHeight())
                break;
        }
        if (index == fmts.length) {
            Toast.makeText(getApplicationContext(), "摄像头分辨率不支持",
                    Toast.LENGTH_SHORT).show();
			Log.e(TAG, "not SupportedFormats");
            return;
        }
		Log.e(TAG, "startVideo");
        startVideo();
        mEncoderCaptureButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mEncoderCapturing = !mEncoderCapturing;
                if (mEncoderCapturing) {
                    mEncoderCaptureButton.setText("录制中...");
                    avMuxer.setRecordTime(timeLimit);
                    avMuxer.creatMuxer();
                    mVideoInput.setResult(0);
                    startChatAudio();
                    mCountdown = timeLimit * 60 * 1000;
                    mTimerDisplay.setVisibility(View.VISIBLE);
                    handler.postDelayed(runnable, 100);
                    isRunnable = true;
                    Toast.makeText(getApplicationContext(), "开始录制...",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mEncoderCaptureButton.setText("录制");
                    mAudioLoop.setRecording(false);
                    avMuxer.stopMuxer();
                    while (audioRecoderInput != null) {
                        if (!audioRecoderInput.isAlive())
                            break;
                        try {
                            Thread.sleep(5);
                        } catch (Exception e) {
                            Log.e(TAG, "audioRecod thread wait" + e.getMessage());
                        }
                    }
                    audioRecoderInput = null;
                    if (isRunnable == true) {
                        isRunnable = false;
                        handler.removeCallbacks(runnable);
                        mTimerDisplay.setVisibility(View.GONE);
                    }
                    Log.e(TAG,"audioRecoderInput stop successful");
                    Toast.makeText(getApplicationContext(), "录制保存完成！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startVideo()
    {
        /*mDecodeSfc.setVisibility(View.VISIBLE);
        mPreviewSfc.setVisibility(View.VISIBLE);*/
        startLatencyTracking();
        if (!ENCODER_ONLY)
        {
            mVideoOutput.setShowView(mDecodeSfc);
            mVideoOutput.open(mVideoFormatInfo, false);
            mVideoOutput.start();
            Log.e(TAG, "start video decorder over!");
        }
        mVideoInput.setShowView(mPreviewSfc);
		Log.e(TAG, "mVideoInput.open");
        mVideoInput.open(mVideoFormatInfo, USE_SW_ENCODER);
        /*if(mEncoderCapturing) {
            mVideoInput.setMediaMuxer(mMediaMuxer);
        }*/
        //mVideoInput.setCaptureFlag(mEncoderCapturing);
        mVideoInput.start();
        Log.e(TAG, "start video encoder over!");

        mIsStarted = true;
    }
    private void startChatAudio()
    {
        mAudioLoop.initAudioEncoder();
        mAudioLoop.setRecording(true);
        Log.d(TAG, "local audio loop started");
        if (audioRecoderInput != null)
            if (!audioRecoderInput.isAlive())
                audioRecoderInput = null;
        if (audioRecoderInput == null) {
            Log.e(TAG, "audioRecoderInput create");
            audioRecoderInput = new Thread(mAudioLoop);
        }
        if (!audioRecoderInput.isAlive()) {
            Log.e(TAG, "audioRecoderInput is not alive,need start");
            audioRecoderInput.start();
        }
        Log.e(TAG, "audioRecoderInput start successful");
    }

    private void stopVideo()
    {
        mIsStarted = false;

        mVideoInput.stop();
        mVideoInput.close();
        avMuxer.stopMuxer();
        if (!ENCODER_ONLY)
        {
            mVideoOutput.stop();
            mVideoOutput.close();
        }
        mDecodeSfc.setVisibility(View.INVISIBLE);
        mPreviewSfc.setVisibility(View.INVISIBLE);
    }

    private void updateStats()
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                mStatsFrame.removeAllViews();
                if (mIsStarted) {
                    if ((isRunnable == true) && (mCountdown < 0)) {
                        isRunnable = false;
                        handler.removeCallbacks(runnable);
                        mTimerDisplay.setVisibility(View.GONE);
                    }
                    //if (isShowStatus.equals("enable")) {
                    String videoFormat = Integer.toString(mVideoFormatInfo
                            .getWidth())
                            + "x"
                            + Integer.toString(mVideoFormatInfo.getHeight());
                    addLineToStats("编码分辨率：" + videoFormat);
                    addLineToStats("编码帧率 ：" + Integer.toString(getThroughputCapture1()) + "fps");
                    addLineToStats("编码格式： " + mVideoFormatInfo.getMimeType());
                    addLineToStats("视频码率：" + Integer.toString(getBitrate_Avg()) + "kbps");
                    /*addLineToStats("Time Statistics (camera / encoder / decoder / total):");
                    addLineToStats("First Frame Delay = "
                            + Integer.toString(getStartupLatencyCam()) + "ms"
                            + " / "
                            + Integer.toString(getStartupLatencyCapture())
                            + "ms" + " / "
                            + Integer.toString(getStartupLatencyPlayback())
                            + "ms" + " / "
                            + Integer.toString(getStartupLatencyPlayback())
                            + "ms");

                    addLineToStats("Average Latency = " + "NA" + " / "
                            + Integer.toString(getAverageLatencyCapture())
                            + "ms" + " / "
                            + Integer.toString(getAverageLatencyPlayback())
                            + "ms" + " / "
                            + Integer.toString(getAverageLatencyTotal()) + "ms");
                    addLineToStats("编码帧率："
                            + Integer.toString(getThroughputCam()) + "fps"
                            + " / " + Integer.toString(getThroughputCapture())
                            + "fps" + " / "
                            + Integer.toString(getThroughputTotal()) + "fps"
                            + " / " + Integer.toString(getThroughputTotal())
                            + "fps");
                    addLineToStats("real_Time_BitrateEnc = " + Integer.toString(getBitrate()) + "Kb/s");
                    addLineToStats("Avg_BitrateEnc = " + Integer.toString(getBitrate_Avg()) + "Kb/s");*/
                }
            }
        });
    }

    private void addLineToStats(String line)
    {
        TextView tv = new TextView(this);
        tv.setTextColor(Color.RED);
        tv.setText(line);
        mStatsFrame.addView(tv);
    }

    private class VideoTag
    {
        public long pts;
        public long nanoTimeStart;
        //public long nanoTimeStartDec;
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

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mCountdown = mCountdown -100;
            if (mCountdown < 0) {
                if (mEncoderCapturing)
                    mEncoderCapturing = false;
                mEncoderCaptureButton.setText("录制");
                mTimerDisplay.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "录制保存完成！",
                        Toast.LENGTH_SHORT).show();
            } else {
                mTimerDisplay.setText("录制" + timeLimit + "分钟倒计时：" + getFormatHMS(mCountdown));
                handler.postDelayed(this, 100);
            }
        }
    };
    private String getFormatHMS(long time){
        int ms= (int) ((time%1000)/10);//10毫秒
        int s= (int) ((time%60000)/1000);//秒
        int m=(int) ((time%3600000)/60000);//分
        int h=(int) (time/3600000);//时
        return String.format("%02d:%02d:%02d.%02d",h,m,s,ms);
    }
    private static int mFrameCount = 0;
    private static int mFrameCountEnc = 0;
    private static int mFrameCountCam = 0;
    private static long mNanoTimeStart = 0;
    private static long mNanoTimeStartDec = 0;
    private static long mNanoTimeStartEnc = 0;
    private static long mNanoTimeStartCam = 0;
    private static long mStartLatencyDec = 0;
    private static long mStartLatencyEnc = 0;
    private static long mStartLatencyCam = 0;
    private static long mLatencyTotal = 0;
    private static long mLatencyDec = 0;
    private static long mLatencyEnc = 0;
    private static long mAverageLatencyTotal = 0;
    private static long mAverageLatencyDec = 0;
    private static long mAverageLatencyEnc = 0;
    private static int mThroughput = 0;
    private static int mThroughputEnc = 0;
    private static int mThroughputCam = 0;
    private static long mPreviousPTS = -1;
    private static long mPreviousPTSEnc = -1;
    private static long mPreviousPTSDec = -1;
    private static int mBitrate = 0;
    private static long mBitrate_All = 0;
    private static int mBitrateEnc = 0;
    private static int mBitrate_Avg = 0;
    private static int Bitratecount = 0;
    private static long mStartBitrateEnc = 0;
    private static long mFrameCountEnc_Bitrate = 0;
    private static LinkedList<VideoTag> videoTagQ = new LinkedList<VideoTag>();
    private static LinkedList<VideoTag> videoDecoderTagQ = new LinkedList<VideoTag>();

    private static void startLatencyTracking()
    {
        mBitrate = 0;
        mBitrateEnc = 0;
        mBitrate_All = 0;
        mBitrate_Avg = 0;
        Bitratecount = 0;
        mStartBitrateEnc = 0;
        mFrameCountEnc_Bitrate = 0;
        mFrameCount = 0;
        mFrameCountEnc = 0;
        mFrameCountCam = 0;
        mNanoTimeStart = System.nanoTime();
        mNanoTimeStartEnc = 0;
        mNanoTimeStartDec = 0;
        mNanoTimeStartCam = 0;
        mStartLatencyDec = 0;
        mStartLatencyEnc = 0;
        mStartLatencyCam = 0;
        mLatencyTotal = 0;
        mLatencyDec = 0;
        mLatencyEnc = 0;
        mAverageLatencyTotal = 0;
        mAverageLatencyDec = 0;
        mAverageLatencyEnc = 0;
        mThroughput = 0;
        mThroughputEnc = 0;
        mThroughputCam = 0;
        mPreviousPTS = -1;
        videoTagQ.clear();
    }

    private static int getAverageLatencyCapture()
    {
        return (int) (mAverageLatencyEnc / 1000000);
    }

    private static int getAverageLatencyPlayback()
    {
        return (int) (mAverageLatencyDec / 1000000);
    }

    private static int getAverageLatencyTotal()
    {
        return (int) (mAverageLatencyTotal / 1000000);
    }

    private static int getStartupLatencyCam()
    {
        return (int) (mStartLatencyCam / 1000000);
    }

    private static int getStartupLatencyCapture()
    {
        return (int) (mStartLatencyEnc / 1000000);
    }

    private static int getStartupLatencyPlayback()
    {
        return (int) (mStartLatencyDec / 1000000);
    }

    private static int getThroughputCam()
    {
        return mThroughputCam;
    }

    private static int getThroughputCapture1()
    {
        if (FRAME_Rate > 0) {
            if (mThroughputEnc == FRAME_Rate - 2)
                return mThroughputEnc + 1;
            else if ((mThroughputEnc < (FRAME_Rate - 2)) && mThroughputEnc > 0)
                return mThroughputEnc + 2;
        }
        return mThroughputEnc;
    }

    private static int getThroughputCapture()
    {
        return mThroughputEnc;
    }

    private static int getThroughputTotal()
    {
        return mThroughput;
    }

    private static int getBitrate() {return mBitrateEnc;}

    private static int getBitrate_Avg() {return mBitrate_Avg;}

    private class VideoCallback implements IVideoDevice.IVideoDeviceCallback
    {

        private boolean mInput;

        public VideoCallback(boolean input)
        {
            mInput = input;
        }

        @Override
        public void onFrameRecv(long pts)
        {
            if (mInput)
            {
                long currTime = System.nanoTime();
                if (mNanoTimeStartCam == 0)
                {
                    mNanoTimeStartCam = currTime;
                }
                updateCamThroughput(currTime);
            }
        }

        @Override
        public void onFrameIn(long pts)
        {
            long currTime = System.nanoTime();
            if (mInput)
            {
                if (mNanoTimeStartEnc == 0)
                {
                    mNanoTimeStartEnc = currTime;
                }
                addNewTag(pts, currTime);
            }
            else
            {
                if (mNanoTimeStartDec == 0)
                {
                    mNanoTimeStartDec = currTime;
                }
                //setTagDecStartTime(pts, currTime);
                addNewDecoderTagQ(pts, currTime);
            }
        }

        @Override
        public void onFrameOut(long pts , int bitrate)
        {
            long currTime = System.nanoTime();
            if (mInput)
            {
                if (isOutFrameValid(pts))
                {
                    updateEncLatency(pts, currTime);
                    updateEncThroughput(currTime, bitrate);
                }
                //if (ENCODER_ONLY)
                //{
                    removeTag(pts);
                //}
            }
            else
            {
                //if ((mPreviousPTS != pts) && (mPreviousPTS != (pts - 10)))
                //{
                    if (isOutFrameDecoderValid(pts))
                    {
                        updateDecLatency(pts, currTime);
                        updateDecThroughput(currTime);
                    }
                    //removeTag(pts);
                    removeDecoderTagQ(pts);
                //}
                //mPreviousPTS = pts;
            }
        }

        @Override
        public void onFrameConsumed(long pts)
        {
        }

        private void addNewTag(long pts, long currTime)
        {
            synchronized (videoTagQ)
            {
                VideoTag tag = new VideoTag();
                tag.pts = pts;
                tag.nanoTimeStart = currTime;
                videoTagQ.add(tag);
            }
        }

        private void removeTag(long pts)
        {
            synchronized (videoTagQ)
            {
                while (!videoTagQ.isEmpty())
                {
                    // ignore bogus frames on startup
                    VideoTag tag = videoTagQ.peek();
                    if (tag != null)
                    {
                        if (pts < (tag.pts - 10))
                        {
                            break;
                        }
                    }

                    tag = videoTagQ.pop();
                    if ((tag.pts == pts) || (tag.pts == (pts + 10)))
                    {
                        break;
                    }
                }
            }
        }

        private void addNewDecoderTagQ(long pts, long currTime) {
            synchronized (videoDecoderTagQ) {
                VideoTag tag = new VideoTag();
                tag.pts = pts;
                tag.nanoTimeStart = currTime;
                videoDecoderTagQ.add(tag);
            }
        }

        private void removeDecoderTagQ(long pts)
        {
            synchronized (videoDecoderTagQ)
            {
                while (!videoDecoderTagQ.isEmpty())
                {
                    // ignore bogus frames on startup
                    VideoTag tag = videoDecoderTagQ.peek();
                    if (tag != null)
                    {
                        if (pts < (tag.pts - 10))
                        {
                            break;
                        }
                    }

                    tag = videoDecoderTagQ.pop();
                    if ((tag.pts == pts) || (tag.pts == (pts + 10)))
                    {
                        break;
                    }
                }
            }
        }

        private boolean isOutFrameValid(long pts)
        {
            synchronized (videoTagQ)
            {
                ListIterator<VideoTag> it = videoTagQ.listIterator(0);
                while (it.hasNext())
                {
                    VideoTag tag = it.next();
                    if ((tag.pts == pts) || (tag.pts == (pts + 10)))
                    {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean isOutFrameDecoderValid(long pts)
        {
            synchronized (videoDecoderTagQ)
            {
                ListIterator<VideoTag> it = videoDecoderTagQ.listIterator(0);
                while (it.hasNext())
                {
                    VideoTag tag = it.next();
                    if ((tag.pts == pts) || (tag.pts == (pts + 10)))
                    {
                        return true;
                    }
                }
            }
            return false;
        }

        /*private void setTagDecStartTime(long pts, long currTime)
        {
            synchronized (videoTagQ)
            {
                ListIterator<VideoTag> it = videoTagQ.listIterator(0);
                while (it.hasNext())
                {
                    VideoTag tag = it.next();
                    if ((tag.pts == pts) || (tag.pts == (pts + 10)))
                    {
                        tag.nanoTimeStartDec = currTime;
                        break;
                    }
                }
            }
        }*/

        private void updateEncLatency(long pts, long currTime)
        {
            synchronized (videoTagQ)
            {
                ListIterator<VideoTag> it = videoTagQ.listIterator(0);
                while (it.hasNext())
                {
                    VideoTag tag = it.next();
                    if ((tag.pts == pts) || (tag.pts == (pts + 10)))
                    {
                        mLatencyEnc = mLatencyEnc
                                + (currTime - tag.nanoTimeStart);
                        break;
                    }
                }
            }
        }

        private void updateDecLatency(long pts, long currTime)
        {
            synchronized (videoDecoderTagQ)
            {
                ListIterator<VideoTag> it = videoDecoderTagQ.listIterator(0);
                while (it.hasNext())
                {
                    VideoTag tag = it.next();
                    if ((tag.pts == pts) || (tag.pts == (pts + 10)))
                    {
                        //mLatencyTotal = mLatencyTotal
                                //+ (currTime - tag.nanoTimeStart);
                        //mLatencyDec = mLatencyDec
                                //+ (currTime - tag.nanoTimeStartDec);
                        mLatencyDec = mLatencyDec + (currTime - tag.nanoTimeStart);
                        break;
                    }
                }
            }
        }

        private void updateCamThroughput(long currTime)
        {
            if (mStartLatencyCam == 0)
            {
                mStartLatencyCam = currTime - mNanoTimeStart;
                updateStats();
            }
            if (++mFrameCountCam == FRAME_THROUGHPUT_INTERVAL)
            {

                int elapsedMS = (int) ((currTime - mNanoTimeStartCam) / 1000000);
                if (elapsedMS > 0)
                {
                    mThroughputCam = (int) (mFrameCountCam * 1000 / elapsedMS);
                }

                mFrameCountCam = 0;
                mNanoTimeStartCam = currTime;

                updateStats();
            }
        }

        private void updateEncThroughput(long currTime, int bitrate)
        {
            if (mStartLatencyEnc == 0)
            {
                mStartLatencyEnc = currTime - mNanoTimeStart;
                updateStats();
            }
            mBitrate = mBitrate + bitrate;
            if (++mFrameCountEnc == FRAME_THROUGHPUT_INTERVAL)
            {
                ++ Bitratecount;
                mAverageLatencyEnc = mLatencyEnc / mFrameCountEnc;

                int elapsedMS = (int) ((currTime - mNanoTimeStartEnc) / 1000000);
                if (elapsedMS > 0)
                {
                    mThroughputEnc = (int) (mFrameCountEnc * 1000 / elapsedMS);
                    mBitrateEnc = (int) (mBitrate * 8 / elapsedMS);
                    mBitrate_All = mBitrate_All + mBitrateEnc;
                    mBitrate_Avg = (int) (mBitrate_All/Bitratecount);
                }
                mBitrate = 0;
                mFrameCountEnc = 0;
                mLatencyEnc = 0;
                mNanoTimeStartEnc = currTime;

                updateStats();
            }
        }

        private void updateDecThroughput(long currTime)
        {
            if (mStartLatencyDec == 0)
            {
                mStartLatencyDec = currTime - mNanoTimeStart;
                updateStats();
            }
            if (++mFrameCount == FRAME_THROUGHPUT_INTERVAL)
            {
                //mAverageLatencyTotal = mLatencyTotal / mFrameCount;
                mAverageLatencyDec = mLatencyDec / (mFrameCount * 3);
                mAverageLatencyTotal = mAverageLatencyDec + mAverageLatencyEnc;
                int elapsedMS = (int) ((currTime - mNanoTimeStartDec) / 1000000);
                if (elapsedMS > 0)
                {
                    mThroughput = (int) (mFrameCount * 1000 / elapsedMS);
                }

                mFrameCount = 0;
                mLatencyTotal = 0;
                mLatencyDec = 0;
                mNanoTimeStartDec = currTime;

                updateStats();
            }
        }
    }

    public static class Sample
    {

        public final String name;
        public final String contentId;
        public final String uri;
        public final int type;

        public Sample(String name, String uri, int type)
        {
            this(name, name.toLowerCase(Locale.US).replaceAll("\\s", ""), uri,
                    type);
        }

        public Sample(String name, String contentId, String uri, int type)
        {
            this.name = name;
            this.contentId = contentId;
            this.uri = uri;
            this.type = type;
        }

    }
    private class CameraIsExist extends BroadcastReceiver{
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                File camera = new File("/dev/video0");
                if (!camera.exists() && isCameraExist) {
                    isCameraExist = false;
                    Toast.makeText(getApplicationContext(), "摄像头已拔出",
                            Toast.LENGTH_SHORT).show();
                    mAudioLoop.setRecording(false);
                    stopVideo();
                    mAudioLoop.audioStop();
                    while (audioRecoderInput != null) {
                        if (!audioRecoderInput.isAlive())
                            break;
                        try {
                            Thread.sleep(5);
                        } catch (Exception e) {
                            Log.e(TAG, "audioRecod thread wait" + e.getMessage());
                        }
                    }
                    Log.e(TAG,"audioRecoderInput stop successful");
                    audioRecoderInput = null;
                    timeLimit = -1;
                    if (isRunnable == true) {
                        isRunnable = false;
                        handler.removeCallbacks(runnable);
                        mTimerDisplay.setVisibility(View.GONE);
                    }
                    runnable = null;
                    handler = null;
                    VideoCapture.Instance().closeCamera();
                    Intent newIntent = new Intent(VideoPlayerActivity.this, MainActivity.class);
                    startActivity(newIntent);
                    finish();
                }
            }
        }
    }
}
