package com.yeungeek.avsample.activities.media

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yeungeek.avsample.R
import com.yeungeek.avsample.databinding.ActivityMediaMuxerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.nio.ByteBuffer
import kotlin.math.max

class MediaMuxerActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMediaMuxerBinding
    private val muxerFilePath: String

    private val recordDir: File = File(
        "${Environment.getExternalStorageDirectory().absolutePath}" + File.separator + "media"
                + File.separator + "record"
    )

    init {
        val filePath =
            File(
                "${Environment.getExternalStorageDirectory().absolutePath}" + File.separator + "media"
                        + File.separator + "muxer"
            )
        filePath.mkdirs()
        muxerFilePath =
            "$filePath" + File.separator + "media_muxer_" + System.currentTimeMillis() + ".mp4"
        Timber.d("##### recorder file path $muxerFilePath")
    }

    private val mediaMuxer: MediaMuxer by lazy {
        MediaMuxer(muxerFilePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4).apply {
            setOrientationHint(90)
        }
    }

    private val readBuffer: ByteBuffer by lazy {
        ByteBuffer.allocate(500 * 1024)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaMuxerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.startMuxerBtn.setOnClickListener(this)


    }

    private fun findFormat(mediaExtractor: MediaExtractor, prefix: String): MediaFormat? {
        for (i in 0 until mediaExtractor.trackCount) {
            val format = mediaExtractor.getTrackFormat(i)
            val mime = format.getString("mime")
            if (mime!!.startsWith(prefix)) {
                return format
            }
        }
        return null
    }

    private fun findTrackIndex(mediaExtractor: MediaExtractor, prefix: String): Int {
        for (i in 0 until mediaExtractor.trackCount) {
            val format = mediaExtractor.getTrackFormat(i)
            val mime = format.getString("mime")
            if (mime!!.startsWith(prefix)) {
                return i
            }
        }
        return -1
    }

    private fun startMuxer() {
        Timber.d("##### start muxer")
        lifecycleScope.launch(Dispatchers.IO) {
            var files = recordDir.listFiles { file -> file.name.endsWith(".mp4") }

            var audioFormat: MediaFormat? = null
            var videoFormat: MediaFormat? = null

            var findAudioFormat = false
            var findVideoFormat = false

            for (file in files) {
                Timber.d("##### file: ${file.name}")

                val mediaExtractor = MediaExtractor()
                mediaExtractor.setDataSource(file.absolutePath)

                if (!findAudioFormat) {
                    audioFormat = findFormat(mediaExtractor, "audio/")
                    Timber.d("##### audio format ${audioFormat.toString()}")
                    findAudioFormat = audioFormat != null
                }

                if (!findVideoFormat) {
                    videoFormat = findFormat(mediaExtractor, "video/")
                    Timber.d("##### video format ${videoFormat.toString()}")
                    findVideoFormat = videoFormat != null
                }

                mediaExtractor.release()
                if (findAudioFormat && findVideoFormat) {
                    break
                }
            }

            var mediaMuxerAudioTrackIndex = 0
            if (findAudioFormat) {
                mediaMuxerAudioTrackIndex = mediaMuxer.addTrack(audioFormat!!)
            }

            var mediaMuxerVideoTrackIndex = 0
            if (findVideoFormat) {
                mediaMuxerVideoTrackIndex = mediaMuxer.addTrack(videoFormat!!)
            }

            mediaMuxer.start()

            //pts
            var audioPts = 0L
            var videoPts = 0L

            for (file in files) {
                Timber.d("##### muxer file: ${file.name}")

                var hasAudio = false
                var hasVideo = false

                val audioMediaExtractor = MediaExtractor()
                audioMediaExtractor.setDataSource(file.absolutePath)

                val audioTrackIndex = findTrackIndex(audioMediaExtractor, "audio/")
                if (audioTrackIndex >= 0) {
                    audioMediaExtractor.selectTrack(audioTrackIndex)
                    hasAudio = true
                }

                val videoMediaExtractor = MediaExtractor()
                val videoTrackIndex = findTrackIndex(videoMediaExtractor, "video/")
                if (videoTrackIndex >= 0) {
                    videoMediaExtractor.selectTrack(videoTrackIndex)
                    hasVideo = true
                }

                if (!hasAudio && !hasVideo) {
                    audioMediaExtractor.release()
                    videoMediaExtractor.release()
                    continue
                }

                if (hasAudio) {
                    var hasDone = false
                    var lastPts = 0L

                    while (!hasDone) {
                        readBuffer.rewind()
                        val frameSize = audioMediaExtractor.readSampleData(readBuffer, 0)
                        Timber.d("##### audio frameSize $frameSize")

                        if (frameSize < 0) {
                            hasDone = true
                        } else {
                            val bufferInfo = MediaCodec.BufferInfo()
                            bufferInfo.offset = 0
                            bufferInfo.size = frameSize
                            bufferInfo.presentationTimeUs =
                                audioPts + audioMediaExtractor.sampleTime

                            if ((audioMediaExtractor.sampleFlags and MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0) {
                                bufferInfo.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME
                            }

                            readBuffer.rewind()
                            mediaMuxer.writeSampleData(
                                mediaMuxerAudioTrackIndex,
                                readBuffer,
                                bufferInfo
                            )
                            audioMediaExtractor.advance()
                            if (audioMediaExtractor.sampleTime > 0) {
                                lastPts = audioMediaExtractor.sampleTime
                            }
                        }
                    }

                    audioPts += lastPts
                    audioMediaExtractor.release()
                }

                //video
                if (hasVideo) {
                    var hasDone = false
                    var lastPts = 0L

                    while (!hasDone) {
                        readBuffer.rewind()
                        val frameSize = videoMediaExtractor.readSampleData(readBuffer, 0)
                        if (frameSize < 0) {
                            hasDone = true
                        } else {
                            val bufferInfo = MediaCodec.BufferInfo()
                            bufferInfo.offset = 0
                            bufferInfo.size = frameSize
                            bufferInfo.presentationTimeUs =
                                videoPts + videoMediaExtractor.sampleTime

                            if ((videoMediaExtractor.sampleFlags and MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0) {
                                bufferInfo.flags = MediaCodec.BUFFER_FLAG_KEY_FRAME
                            }

                            readBuffer.rewind()
                            mediaMuxer.writeSampleData(
                                mediaMuxerVideoTrackIndex,
                                readBuffer,
                                bufferInfo
                            )
                            videoMediaExtractor.advance()
                            if (videoMediaExtractor.sampleTime > 0) {
                                lastPts = videoMediaExtractor.sampleTime
                            }
                        }
                    }
                    videoPts += lastPts
                    videoMediaExtractor.release()
                }

                //time
                audioPts = max(audioPts, videoPts)
                videoPts = audioPts
            }

            try {
                mediaMuxer.stop()
                mediaMuxer.release()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start_muxer_btn -> {
                startMuxer()
            }
        }
    }
}