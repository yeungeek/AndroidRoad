package com.android.avPipe;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.util.Log;

import java.nio.ByteBuffer;

//import android.os.SystemProperties;
public class LocalAudioLoopThread implements Runnable
{
	/** Called when the activity is first created. */
	//	Button btnRecord, btnStop, btnExit;
	//	SeekBar skbVolume;// 调节音量
	private MediaCodec mAudioEncoder;
	private AudioFormatInfo mAudioFormatInfo;
	private ByteBuffer[] mAudioEncodeInputBuffer;
	private AudioEncoderOutputThread mAudioOutput;
	//private ByteBuffer[] mAudioEncodeOutputBuffer;
	boolean isRecording = false;// 是否录放的标记
	private int limitSize = 4096;
	private int mReadSize = 0;
	private int mReadPos  = 0;
	private byte[] mBuffer = null;
	private ByteBuffer emptyBuffer = ByteBuffer.allocate(limitSize);
	static final int frequency = 48000;
	static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	int recBufSize, playBufSize;
	AudioRecord audioRecord = null;
	AudioTrack audioTrack;
	private static final String TAG = "LocalAudioLoopThread";
	private float mMicVolume;
	//private BlockingQueue<ByteBuffer> UnencodedAudioFrames= new ArrayBlockingQueue<ByteBuffer>(10);
	public float getmMicVolume()
	{
		return mMicVolume;
	}

	public void setmMicVolume(float mMicVolume)
	{
		this.mMicVolume = mMicVolume;
		audioTrack.setStereoVolume(mMicVolume,mMicVolume);
	}

	public LocalAudioLoopThread()
	{
		Log.d(TAG, "LocalAudioLoopThread Ctor");
		recBufSize = AudioRecord.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);
		if (recBufSize != AudioRecord.ERROR_BAD_VALUE)
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
					channelConfiguration, audioEncoding, recBufSize);
		//playBufSize = AudioTrack.getMinBufferSize(frequency,
		//		channelConfiguration, audioEncoding);
		if (audioRecord == null) {
			Log.e(TAG, "AudioRecord init fail");
		}
		/*new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelConfiguration, audioEncoding, recBufSize);*/
		mAudioFormatInfo = new AudioFormatInfo(2, 48000, 128000);
		//initAudioEncoder();
		//		SystemProperties.set("audio.output.double_output", "1");
		/*audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
				channelConfiguration, audioEncoding, playBufSize,
				AudioTrack.MODE_STREAM);*/
	}

	// ------------------------------------------
	// btnRecord = (Button) this.findViewById(R.id.btnRecord);
	// btnRecord.setOnClickListener(new ClickEvent());
	//
	// btnStop = (Button) this.findViewById(R.id.btnStop);
	// btnStop.setOnClickListener(new ClickEvent());
	//
	// btnExit = (Button) this.findViewById(R.id.btnExit);
	// btnExit.setOnClickListener(new ClickEvent());
	//
	// skbVolume = (SeekBar) this.findViewById(R.id.skbVolume);
	// skbVolume.setMax(100);// 音量调节的极限
	// skbVolume.setProgress(70);// 设置seekbar的位置值
	//
	// audioTrack.setStereoVolume(0.7f, 0.7f);// 设置当前音量大小
	// skbVolume.setOnSeekBarChangeListener(new
	// SeekBar.OnSeekBarChangeListener()
	// {
	// @Override
	// public void onStopTrackingTouch(SeekBar seekBar)
	// {
	// float vol = (float) (seekBar.getProgress())
	// / (float) (seekBar.getMax());
	// audioTrack.setStereoVolume(vol, vol);// 设置音量
	// }
	//
	// @Override
	// public void onStartTrackingTouch(SeekBar seekBar)
	// {
	// // TODO Auto-generated method stub
	// }
	//
	// @Override
	// public void onProgressChanged(SeekBar seekBar,
	// int progress, boolean fromUser)
	// {
	// // TODO Auto-generated method stub
	// }
	// });
	// }

	// @Override
	// protected void onDestroy()
	// {
	// super.onDestroy();
	// android.os.Process.killProcess(android.os.Process.myPid());
	// }

	// class ClickEvent implements View.OnClickListener
	// {
	//
	// @Override
	// public void onClick(View v)
	// {
	// if (v == btnRecord)
	// {
	// isRecording = true;
	// new RecordPlayThread().start();// 开一条线程边录边放
	// } else if (v == btnStop)
	// {
	// isRecording = false;
	// } else if (v == btnExit)
	// {
	// isRecording = false;
	// ZhuTingQiActivity.this.finish();
	// }
	// }
	// }
	public void run()
	{
		try
		{
			long generateIndex = 0;
			if (mBuffer == null)
				mBuffer = new byte[recBufSize];
			//byte[] buffer = new byte[recBufSize];
			if (audioRecord == null) {
				recBufSize = AudioRecord.getMinBufferSize(frequency,
						channelConfiguration, audioEncoding);
				if (recBufSize != AudioRecord.ERROR_BAD_VALUE)
					audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
							channelConfiguration, audioEncoding, recBufSize);
			}
			audioRecord.startRecording();// 开始录制
			if (mAudioEncoder == null)
				initAudioEncoder();
			if (mAudioOutput == null)
				mAudioOutput = new AudioEncoderOutputThread(mAudioEncoder);
			if (mAudioOutput.getAudioEncoder() == null)
				mAudioOutput.setAudioEncoder(mAudioEncoder);
			mAudioOutput.setRecording(isRecording);
			mAudioEncoder.start();
			Log.e(TAG,"mAudioEncoder start");
			mAudioEncodeInputBuffer = mAudioEncoder.getInputBuffers();
			if (!mAudioOutput.isAlive()) {
				Log.e(TAG, "mAudioOutput is not Alive, need start!");
				mAudioOutput.start();
			}
			Log.e(TAG, "audioEncoderOutput start successful");
			Log.e(TAG,"mAudioOutput start");
			//mAudioEncodeOutputBuffer = mAudioEncoder.getOutputBuffers();
			int waitTimeUs = 10000;
			int index = -1;
			//audioTrack.play();// 开始播放
			Log.d(TAG, "local audio loop thread started");
			while (isRecording())
			{
				//UnencodedAudioFrames.put(ByteBuffer.wrap(buffer));
				try {
					index = mAudioEncoder.dequeueInputBuffer(waitTimeUs);
					Log.e(TAG, "mAudioEncoder.dequeueInputBuffer:" + index);
					if (index < 0)
						continue;
				} catch (Exception e) {
					Log.e(TAG, "mAudioEncoder.dequeueInputBuffer:" + e.getMessage());
					continue;
				}
				//Log.e(TAG, "audio: inputBuffer.size: %d" + mAudioEncodeInputBuffer[index].capacity() + "channel: %d" + mAudioFormatInfo.getChannelCount());

				if (index >= 0) {
					//mAudioEncodeInputBuffer[index].rewind();
					mAudioEncodeInputBuffer[index].clear();
				}
				// 从MIC保存数据到缓冲区
				//int bufferReadResult = audioRecord.read(mBuffer, 0, recBufSize/2);
				//byte[] buffer = new byte[bufferReadResult * 2];
				//System.arraycopy(mBuffer, 0, buffer, 0, bufferReadResult);
				//System.arraycopy(mBuffer, 0, buffer, bufferReadResult, bufferReadResult);
				int bufferReadResult = read(mAudioEncodeInputBuffer[index]);
				generateIndex += bufferReadResult;
				//Log.e(TAG,"audioRecord read" + bufferReadResult);
				//mAudioEncodeInputBuffer[index].put(mBuffer.clone());
				long ptsUsec = computePresentationTime(generateIndex);
				//if (currentTimeUs == 0)
				//	currentTimeUs = System.nanoTime() / 1000;
				//long ptsUsec = System.nanoTime() / 1000 - currentTimeUs;
				try {
					mAudioEncoder.queueInputBuffer(index, 0, mAudioEncodeInputBuffer[index].capacity(), ptsUsec, 0);
					//Log.e(TAG,"mAudioEncoder queueInputBuffer");
				} catch (Exception e) {
					Log.e(TAG, "mEncodeMediaCodec.queueInputBuffer:" + e.getMessage());
				}
				//byte[] tmpBuf = new byte[bufferReadResult];
				//System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
				// 写入数据即播放
				//audioTrack.write(tmpBuf, 0, tmpBuf.length);
				if (mAudioOutput.getResult() < 0)
					isRecording = false;
			}
			mAudioOutput.setRecording(isRecording);
			audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
			audioStop();
			//audioTrack.stop();
			//audioTrack.release();
		} catch (Throwable t)
		{
			// Toast.makeText(ZhuTingQiActivity.this, t.getMessage(), 1000);
			t.printStackTrace();
		}
	}

	public boolean isRecording()
	{
		return isRecording;
	}

	public void setRecording(boolean isRecording)
	{
		Log.e(TAG, "setRecording is " + isRecording);
		this.isRecording = isRecording;
	}
	public void initAudioEncoder() {
		try {
			mAudioEncoder = MediaCodec.createEncoderByType(mAudioFormatInfo.getMimeType());
			MediaFormat format = MediaFormat.createAudioFormat(mAudioFormatInfo.getMimeType(), mAudioFormatInfo.getSampleRate(), mAudioFormatInfo.getChannelCount());
			format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
			format.setInteger(MediaFormat.KEY_BIT_RATE, mAudioFormatInfo.getBitRate());
			mAudioEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
			mAudioOutput = new AudioEncoderOutputThread(mAudioEncoder);
		} catch (Exception e) {
			Log.e(TAG,"mAudioEncoder create failed:" + e.getMessage());
		}
	}
	public void audioStop() {
		if (audioRecord != null) {
			audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
		}
		if (mAudioEncoder != null) {
			mAudioEncoder.stop();
			mAudioEncoder.release();
			mAudioEncoder = null;
		}
		if (mAudioOutput != null) {
			mAudioOutput.setAudioEncoder(null);
			while (mAudioOutput.isAlive()) {
				try {
					mAudioOutput.setRecording(isRecording);
					Thread.sleep(5);
					Log.e(TAG, "sleep 5ms");
				} catch (Exception e) {
					Log.e(TAG, "mAudioOutput thread wait" + e.getMessage());
				}
			}
			Log.e(TAG, "mAudioOutput stop successful");
			mAudioOutput = null;
		}

	}
	private long computePresentationTime(long PCMSize) {
		return PCMSize * 1000000 / (mAudioFormatInfo.getSampleRate() * mAudioFormatInfo.getChannelCount() * 2);
	}
	/**
	 * The JNI layer will call this method for recored audio data
	 *
	 * @param buf
	 *            data container
	 * @return
	 */
	public int read(ByteBuffer buf) {
		int bytes_read = 0;

		while (isRecording && ((mReadSize == 0) || (AudioRecord.ERROR_INVALID_OPERATION == mReadSize)))
		{
			mReadSize = audioRecord.read(mBuffer, 0, recBufSize);
		}

		if (!isRecording) {
			buf.rewind();
			buf.put(emptyBuffer.array());
			return 0;
		} else {
			if (mReadSize > limitSize) {
				buf.rewind();
				buf.put(mBuffer, mReadPos, limitSize);
				bytes_read = limitSize;
				mReadSize -= limitSize;
				mReadPos  += limitSize;
			} else {
				buf.rewind();
				buf.put(mBuffer, mReadPos, mReadSize);
				bytes_read = mReadSize;
				mReadSize  = 0;
				mReadPos   = 0;
			}
		}
		return bytes_read;
	}
}