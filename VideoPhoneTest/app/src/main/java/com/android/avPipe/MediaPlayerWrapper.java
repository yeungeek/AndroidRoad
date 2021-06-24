package com.android.avPipe;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceView;

import com.rokid.glass.ui.util.Utils;

import java.io.IOException;

public class MediaPlayerWrapper{
	private MediaPlayer mMediaPlayer = null;
	// private SurfaceView mSurfaceView = null ;
	private SurfaceView mSurfaceView = null;
	private final String TAG = "MediaPlayerWrapper";
	private String mSource;
	private int mStatus;
	public static final int STATUS_STOPPED=0;
	public static final int STATUS_STARTED=1;


	public MediaPlayerWrapper(SurfaceView surfaceView, String source) {
		mSurfaceView = surfaceView;
		mSource = source;
		mStatus = STATUS_STOPPED;
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
		    @Override
		    public void onCompletion(MediaPlayer mp)
			{
				mMediaPlayer.stop();
				mMediaPlayer.reset();
				try
				{
					String tmpSource = Utils.getSystemProperty("media.demo.uri");
					if (tmpSource != null && !tmpSource.equals(""))
						mSource = tmpSource;
					Log.d(TAG, "MediaPlayerThread source=" + mSource);
					mMediaPlayer.setDataSource(mSource);
				} catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
				{
					mMediaPlayer.prepare();
				} catch (IllegalStateException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mMediaPlayer.start();
				mStatus = STATUS_STARTED;
		    }
		});
	}

	public void start()
	{
		if (mStatus == STATUS_STARTED)
			return;
		// int idx = Integer.parseInt(System.getProperty("media.demo.hls.index",
		// "0"));
		// Log.d(TAG, "idx="+idx+", source="+sourceList[idx]);
		try
		{
			mMediaPlayer.setDataSource(mSource);
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediaPlayer.setDisplay(mSurfaceView.getHolder());
		try
		{
			mMediaPlayer.prepare();
		}
		catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mMediaPlayer.setOnBufferingUpdateListener(this);
		// mMediaPlayer.setOnCompletionListener(this);
		// mMediaPlayer.setOnPreparedListener(this);
		// mMediaPlayer.setOnVideoSizeChangedListener(this);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.start();
		Log.d(TAG, "Player started");
		mStatus = STATUS_STARTED;
	}
	public void stop()
	{
		if (mStatus == STATUS_STOPPED)
			return;
		mMediaPlayer.stop();
		mMediaPlayer.reset();
		Log.d(TAG, "Player stopped");
		mStatus = STATUS_STOPPED;
	}

	public String getmSource() {
		return mSource;
	}

	public void setmSource(String mSource) {
		this.mSource = mSource;
	}
}
