/*
** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
*/
/*============================================================================
**
**  FILE        AVTypes.java
**
**  PURPOSE     Defines common AV types used for playback/capture
**
**==========================================================================*/

package com.android.avPipe;

/**
 * mocha client native methods
 * <p>
 */
public class AVTypes {

    public static AVTypes getInstance() {
        return SingleAvTypes.singleAvTypes;
    }

    private AVTypes(){}

    private static class SingleAvTypes {
        private static final AVTypes singleAvTypes = new AVTypes();
        private static final EncoderOptimizations singleEncoderOpts = new EncoderOptimizations();
        private static final VideoFmt singleVideoFmt = new VideoFmt();
    }

    public static EncoderOptimizations getEncoderOptsInstance() {
        return SingleAvTypes.singleEncoderOpts;
    }

    public static VideoFmt getVideoFmtInstance() {
        return SingleAvTypes.singleVideoFmt;
    }

    static public class VideoFmt {
        public int width;
        public int height;
        public int framerate;
        public int bitrate;
    }

    static public class EncoderOptimizations {
        public String encodertype = "H.265";
        public int encoderTypePos = 0;
        public int[] resolution = new int[2];
        public int encoderResolutionPos = 0;
        public boolean encoder_request_idr = false;

        //public boolean encoder_bitrate_enable = false;
        public int encoder_bitrate_value = 2048;  // KB
        public int encoderBitRatePos = 0;
        public int encoder_framerate_value = 30;
        public int encodeFrameRatePos = 0;
        public int encoder_iframe_interval = 900;
        //public boolean encoder_iframe_interval_enable = false;

        public int encoder_timeLimit_value = 1;
        public int encoderRecoderPos = 0;
        public void setEncodertype(String type) {
            this.encodertype = type;
        }
        public String getEncodertype() {
            return encodertype;
        }
        public void setResolution(int[] resolution) {
            this.resolution[0] = resolution[0];
            this.resolution[1] = resolution[1];
        }
        public int[] getResolution() {
            return resolution;
        }
        public void setEncoder_bitrate(int value) {
            this.encoder_bitrate_value = value;
        }
        public int getEncoder_bitrate() {
            return encoder_bitrate_value;
        }
        public void setEncoder_framerate(int framerate) {
            this.encoder_framerate_value = framerate;
        }
        public int getEncoder_framerate() {
            return encoder_framerate_value;
        }
        public void setEncoder_iframe(int iframe) {
            this.encoder_iframe_interval = iframe;
        }
        public int getEncoder_iframe() {
            return encoder_iframe_interval;
        }
        public void setEncoder_timeLimit(int recordTime) {
            this.encoder_timeLimit_value = recordTime;
        }
        public int getEncoder_timeLimit() {
            return encoder_timeLimit_value;
        }
    }

}
