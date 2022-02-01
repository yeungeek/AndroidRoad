package com.yeungeek.avsample.activities.media

import android.media.*
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yeungeek.avsample.R
import com.yeungeek.avsample.databinding.ActivityAudioRecorderBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.*
import java.lang.Exception

class AudioRecordActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAudioRecorderBinding
    private lateinit var recordJob: Job
    private var audioRecordFilePath: String
    private var isRecording = false

    companion object {
        const val ENCODE = AudioFormat.ENCODING_PCM_16BIT
        const val CHANNEL_IN_CONFIG = AudioFormat.CHANNEL_IN_MONO
        const val SAMPLE_RATE = 44100
    }

    init {
        val filePath =
            File("${Environment.getExternalStorageDirectory().absolutePath}" + File.separator + "media")
        audioRecordFilePath = "$filePath" + File.separator + "audio_recorder.pcm"
        filePath.mkdirs()
        Timber.d("##### audio recorder file path $audioRecordFilePath")
    }

    private val recordBufferSize: Int by lazy {
        //aac
        AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_IN_CONFIG, ENCODE)
    }

    //MediaRecorder.AudioSource.MIC,
    private val audioRecord: AudioRecord by lazy {
        AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            CHANNEL_IN_CONFIG,
            ENCODE,
            recordBufferSize
        )
    }

    private val audioTrack: AudioTrack by lazy {
        AudioTrack(
            AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build(),
            AudioFormat.Builder().setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .setEncoding(ENCODE).setSampleRate(SAMPLE_RATE).build(),
            recordBufferSize, AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioRecorderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.audioPlayBtn.setOnClickListener(this)
        binding.audioRecordBtn.setOnClickListener(this)


    }

    private fun startRecord() {
        recordJob = lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                Timber.d("##### start recording")
                audioRecord.startRecording()
                val dataOutputStream =
                    DataOutputStream(BufferedOutputStream(FileOutputStream(audioRecordFilePath)))

                val byteArray = ShortArray(recordBufferSize)
                while (isRecording) {
                    val result = audioRecord.read(byteArray, 0, byteArray.size)
                    for (i in 0 until result) {
                        dataOutputStream.writeShort(byteArray[i].toInt())
                    }
                }

                Timber.d("##### stop recording")
                dataOutputStream.flush()
                dataOutputStream.close()
            }
        }
    }

    private fun playRecord() {
        lifecycleScope.launch(Dispatchers.IO) {
            audioTrack.play()

            val dataInputStream =
                DataInputStream(BufferedInputStream(FileInputStream(audioRecordFilePath)))

            val byteArray = ShortArray(recordBufferSize)

            try {
                while (dataInputStream.available() > 0) {
                    var i = 0
                    while (i < byteArray.size) {
                        byteArray[i] = dataInputStream.readShort()
                        i++
                    }

                    audioTrack.write(byteArray, 0, byteArray.size)
                }

                dataInputStream.close()
                audioTrack.stop()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.audio_record_btn -> {
                Timber.d("##### record state: $isRecording")
                if (isRecording) {
                    isRecording = false
                    audioRecord.stop()
                } else {
                    isRecording = true
                    startRecord()
                }
            }

            R.id.audio_play_btn -> {
                playRecord()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        audioRecord?.release()
        audioTrack?.release()
    }
}