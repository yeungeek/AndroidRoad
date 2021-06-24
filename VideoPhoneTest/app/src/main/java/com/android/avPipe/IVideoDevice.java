/*
** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
*/
/*============================================================================
**
**  FILE        IVideoDevice.java
**
**  PURPOSE     Interface to video input/output device
**
**==========================================================================*/

package com.android.avPipe;

/**
 * Discription Here
 * <p>
 */
public interface IVideoDevice {

  public interface IVideoDeviceCallback {
    public void onFrameRecv(long pts);
    public void onFrameIn(long pts);
    public void onFrameOut(long pts, int bitrate);
    public void onFrameConsumed(long pts);
  }

	public void open(VideoFormatInfo vfi, boolean useSoftCodec);

	public void close();

	public void setVideoFormat(VideoFormatInfo vfi);

	public void start();

	public void stop();

	public void abort();

	public int getDevNum();

	public int getCurrentDev();

	public DevInfo getDevInfo(int devId);

	public void setVideoMute(boolean isMute);
}
