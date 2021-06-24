/*
** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
*/
/*============================================================================
**
**  FILE        EncodeSettings.java
**
**  PURPOSE     Handle optimizations to Encoder via MediaCodec interface and
**              any required driver extentions
**
**==========================================================================*/

package com.android.avPipe;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.util.Log;

import com.rokid.glass.ui.util.Utils;

/* Overall goal of this class is to configure the MediaCodec encoder with optimizations.
 * Many of the optimizations require a custom driver on the system. */
public class EncodeSettings {

    private String TAG = "EncodeSettings";
    private MediaCodec mEncoder;
    private boolean mSPSPPSEnabled = false;
    private boolean mSliceSpacingEnabled = false;
    private boolean mInfiniteIFrameEnabled = false;
    private boolean mIntraRefreshEnabled = false;
    private boolean mOptimizedEncoder = false;
    private boolean mRequest_idr = false;
    private int mBitrateKbps = 2000;                    //Kbps

    /* Extension sent to driver when requesting the encoder send only a single I-Frame
     * and wait for the decoder to request additional I-Frames from some other means. */
	private final String INFINITE_I_FRAME = "nPFrames-override";
	private final String INTRAREFRESH_MB_REFRESH = "intra-refresh-AIR-ref";
	private final String INTRAREFRESH_MB_OVERLAP = "intra-refresh-AIR-mbs";
	private final String REQUEST_IDR = "request-idr";
    private final String PREPEND_SPS_PPS_TO_IDR = "prepend-sps-pps-to-idr-frames";

    /* configure the header slice spacing of avc video.  Send the number of macroblocks per slice.
     * NOTE: Currently is limited by macroblocks per row
     * e.g. video with 720.  720/16 = 45.  Must be a multiple of 45. */
	private final String SLICE_SPACING = "macroblock-slice-spacing";

    public EncodeSettings() {
        mOptimizedEncoder = isCustom();
    }

    public void setEncoder(MediaCodec encoder) {  mEncoder = encoder; }

    public void clearSession() {
        // clear any flags stating we have enabled a configuration on an encoder,
        // this is called before each encoder session to accurately portray the settings we have configured.
        mSPSPPSEnabled = false;
        mSliceSpacingEnabled = false;
        mInfiniteIFrameEnabled = false;
        mIntraRefreshEnabled = false;
    }

    public void setDefaults(AVTypes.EncoderOptimizations opts) {
        if (mOptimizedEncoder) {
            //opts.sps_pps_enable = true;
            //opts.infinite_iframe_enable = true;
        }
    }

    /* Note: codec must be running to accept this call.
     * This is a standard Android API */
    public void requestIDRFrame() {
        if (mEncoder != null) {
            Log.v(TAG, "<<<requestIDRRrame()");
            Bundle params = new Bundle();
            params.putInt(MediaCodec.PARAMETER_KEY_REQUEST_SYNC_FRAME, 0);
            mEncoder.setParameters(params);
        } else {
            Log.v(TAG, "requestIDRFrame() failed, no encoder\n");
        }
    }

    /* Note: codec must be running to accept this call.
     * This is a standard Android API */
    public void setBitrate(int bitRateKbps) {
        if (mEncoder != null) {
            Log.v(TAG, "<<<setBitrate( kbps =" + bitRateKbps + ")");
            Bundle params = new Bundle();
            params.putInt(MediaCodec.PARAMETER_KEY_VIDEO_BITRATE, bitRateKbps*1000);
            mEncoder.setParameters(params);
        } else {
            Log.v(TAG, "setBitrate() failed - Encoder not running yet\n");
        }
        mBitrateKbps = bitRateKbps;
    }

    /* Note: This method must be called before calling MediaCodec.configure.
     * This API is also custom and requires ACodec changes so it checks mOptimizedEncoder and fails if not running on a correct device */
    public boolean setStaticSliceSpacing(MediaFormat format, int spacing) {
        if (mOptimizedEncoder) {
            Log.v(TAG, "<<<< MediaCodec.Macroblock Slice Size " + spacing);
            format.setInteger(SLICE_SPACING, spacing);
            mSliceSpacingEnabled = true;
        }
        return mSliceSpacingEnabled;
    }

    public boolean isSliceSpacingEnabled() { return mSliceSpacingEnabled; }

    /* Note: This method must be called before calling MediaCodec.configure.
     * This API is also custom and requires ACodec changes so it checks mOptimizedEncoder and fails if not running on a correct device */
    public boolean setStaticPrependSPSPPS(MediaFormat format) {
        if (mOptimizedEncoder) {
            Log.v(TAG, "<<<< MediaCodec.SPS PPS from Encoder");
            format.setInteger(PREPEND_SPS_PPS_TO_IDR, 1);
            mSPSPPSEnabled = true;
        }
        return mSPSPPSEnabled;
    }
    public boolean isSPSPPSEnabled() { return mSPSPPSEnabled; }

    /* Note: This method must be called before calling MediaCodec.configure.
     * This API is also custom and requires ACodec changes so it checks mOptimizedEncoder and fails if not running on a correct device */
    public boolean setStaticIntraRefresh(MediaFormat format, int refresh, int overlap) {
        if (mOptimizedEncoder) {
            Log.v(TAG, "<<<< MediaCodec.Intrarefresh enable refresh: " + refresh + " overlap: " + overlap);
            format.setInteger("intra-refresh-mode", 1);
            format.setInteger(INTRAREFRESH_MB_REFRESH, refresh);
            format.setInteger(INTRAREFRESH_MB_OVERLAP, overlap);
            mIntraRefreshEnabled = true;
        }
        return mIntraRefreshEnabled;
    }

    /* Note: This method must be called before calling MediaCodec.configure.
     * This API is also custom and requires ACodec changes so it checks mOptimizedEncoder and fails if not running on a correct device */
    public boolean setStaticInfiniteIFrame(MediaFormat format, int nPFrames) {
        if (mOptimizedEncoder) {
            Log.v(TAG, "<<<< MediaCodec.InfiniteIFrame enable " + nPFrames);
            format.setInteger(INFINITE_I_FRAME, nPFrames);
            mInfiniteIFrameEnabled = true;
        }
        return mInfiniteIFrameEnabled;
    }
    public boolean isInfiniteIFrameEnabled() { return mInfiniteIFrameEnabled; }

    private boolean isCustom() {
        /* NOTE: this only works because we build with a custom android.jar */
        if ("true".equals(Utils.getSystemProperty("ro.encoder.optimized"))) {
            return true;
        }
        return false;
    }
}
