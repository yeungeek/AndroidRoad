/*
** Copyright (c) 2014  Amlogic Corporation. All rights reserved.
*/
/*============================================================================
**
**  FILE        AudioFormatInfo.java
**
**  PURPOSE     Encapsulate audio input parameters
**
**==========================================================================*/

package com.android.avPipe;

import android.media.AudioFormat;
import android.media.MediaRecorder.AudioSource;

/**
 * AudioFormat Bean
 *
 * @author Lucky
 *         <p>
 */
public class AudioFormatInfo {
    private final String TAG = "AudioFormatInfo";
    // 采样
    private int sampleRate = 48000;
    private int bitRate = 128000;
    private int channelCount = 2;
    private String mimeType = "audio/mp4a-latm";
    // 声道
    private int channel = AudioFormat.CHANNEL_IN_MONO;
    // 编码格式
    private int format = AudioFormat.ENCODING_PCM_16BIT;
    // 音频
    private int source = AudioSource.VOICE_COMMUNICATION;

    public AudioFormatInfo(int numChannels, int sampleRate, int bitsPerSample) {

        setSampleRate(sampleRate);
        setChannelCount(numChannels);
        setBitRate(bitsPerSample);
        // audio encode
        switch (bitsPerSample) {
        case 128000:
            setFormat(AudioFormat.ENCODING_PCM_16BIT);
            break;
        case 256000:
            setFormat(AudioFormat.ENCODING_PCM_16BIT);
            break;
        default:
            break;
        }

        // channel set
        switch (numChannels) {
        case 1:
            setChannel(AudioFormat.CHANNEL_IN_MONO);
            break;
        case 2:
            setChannel(AudioFormat.CHANNEL_IN_STEREO);
            break;
        default:
            break;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AudioFormatInfo [sampleRate=");
        builder.append(sampleRate);
        builder.append(", channel=");
        if (channel == AudioFormat.CHANNEL_IN_STEREO) {
            builder.append("stereo");
        } else if (channel == AudioFormat.CHANNEL_IN_MONO) {
            builder.append("mono");
        } else {
            builder.append("unknown value!");
        }
        builder.append(", format=");
        if (format == AudioFormat.ENCODING_PCM_16BIT) {
            builder.append("16 bit");
        } else if (format == AudioFormat.ENCODING_PCM_8BIT) {
            builder.append("8 bit");
        }  else {
            builder.append("unknown");
        }
        builder.append(", source=");
        builder.append(source);
        builder.append("]");
        return builder.toString();
    }

    /**
     * default 8000
     *
     * @return
     */
    public int getSampleRate() { return sampleRate; }
    public void setSampleRate(int sampleRate) { this.sampleRate = sampleRate; }

    /**
     * default AudioFormat.CHANNEL_IN_MONO
     *
     * @return
     */
    public int getChannel() { return channel; }
    public void setChannel(int channel) { this.channel = channel; }

    /**
     * default AudioFormat.ENCODING_PCM_16BIT
     *
     * @return
     */
    public int getFormat() { return format; }
    public void setFormat(int format) { this.format = format; }

    /**
     * default AudioSource.VOICE_COMMUNICATION
     *
     * @return
     */
    public int getSource() { return source; }
    public void setSource(int source) { this.source = source; }
    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public int getChannelCount() {
        return channelCount;
    }
    public void setChannelCount(int numChannel) {
        this.channelCount = numChannel;
    }
}
