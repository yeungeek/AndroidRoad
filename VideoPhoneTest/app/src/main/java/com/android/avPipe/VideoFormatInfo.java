/*
** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
*/
/*============================================================================
**
**  FILE        VideoFormatInfo.java
**
**  PURPOSE     Encapsulate video input parameters
**
**==========================================================================*/

package com.android.avPipe;

import android.media.MediaCodecInfo;

/**
 * video format info bean
 * <p>
 */
public class VideoFormatInfo {
	/**
	 * video width
	 */
	private int width = 1920;

	/**
	 * video height
	 */
	private int height = 1080;

	/**
	 * video frame rate
	 */
	private int frameRate = 30;

	/**
	 * video bit rate
	 */
	private int bitRate = 2048000;

	/**
	 * key frame,default is 1 frame per 30 seconds
	 */
	private int iframeInterval = 900;    //one i frame per iframeInterval seconds
	private String mimeType = "video/hevc";
	private int encodeColorFormat = MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar;

	public VideoFormatInfo() { }

	public VideoFormatInfo(int w, int h, int fr) {
		width = w;
		height = h;
		frameRate = fr;
	}
    public static class VFormatSingle {
		private static final VideoFormatInfo mInstance = new VideoFormatInfo();
	}
	public static final VideoFormatInfo getVideoFormatInfo() {
		return VFormatSingle.mInstance;
	}
	public int getEncodeColorFormat() { return encodeColorFormat; }
	public void setEncodeColorFormat(int encodeColorFormat) { this.encodeColorFormat = encodeColorFormat; }

	public String getMimeType() { return mimeType; }
	public void setMimeType(String mimeType) { this.mimeType = mimeType; }

	public int getWidth() { return width; }
	public void setWidth(int width) { this.width = width; }

	public int getHeight() { return height; }
	public void setHeight(int height) { this.height = height; }

	public int getFrameRate() { return frameRate; }
	public void setFrameRate(int frameRate) { this.frameRate = frameRate; }

	public int getBitRate() { return bitRate; }
	public void setBitRate(int bitRate) { this.bitRate = bitRate; }

	public int getIframeInterval() { return iframeInterval; }
	public void setIframeInterval(int iframeInterval) { this.iframeInterval = iframeInterval; }
}
