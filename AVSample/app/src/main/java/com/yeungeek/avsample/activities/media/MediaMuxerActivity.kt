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
    private val sdcardPath = "${Environment.getExternalStorageDirectory().absolutePath}"

    private val recordDir: File =
        File(sdcardPath + File.separator + "media" + File.separator + "record")

    init {
        val filePath = File(
            sdcardPath + File.separator + "media"
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


    private fun startMuxer() {
        Timber.d("##### start muxer")
        lifecycleScope.launch(Dispatchers.IO) {
            var files = recordDir.listFiles { file -> file.name.endsWith(".mp4") }

            var mediaExtractor = MediaExtractor()
            var filePath = files[0].absolutePath
            Timber.d("##### file path: $filePath")
            mediaExtractor.setDataSource(filePath)

            var framerate = 0
            var videoTrackIndex = 0
            for (i in 0 until mediaExtractor.trackCount) {
                var format = mediaExtractor.getTrackFormat(i)
                var mime = format.getString(MediaFormat.KEY_MIME)
                if (mime?.startsWith("video/") == false) {
                    continue
                }

                framerate = format.getInteger(MediaFormat.KEY_FRAME_RATE)
                mediaExtractor.selectTrack(i)

                videoTrackIndex = mediaMuxer.addTrack(format)
                mediaMuxer.start()
            }

            var bufferInfo = MediaCodec.BufferInfo()
            bufferInfo.presentationTimeUs = 0
            var sampleSize = 0
            while (mediaExtractor.readSampleData(readBuffer, 0).also { sampleSize = it } > 0) {
                bufferInfo.offset = 0
                bufferInfo.size = sampleSize
                bufferInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME
                bufferInfo.presentationTimeUs += 1000 * 1000 / framerate
                mediaMuxer.writeSampleData(videoTrackIndex, readBuffer, bufferInfo)
                mediaExtractor.advance()
            }

            mediaExtractor.release()
            mediaMuxer.stop()
            mediaMuxer.release()
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
