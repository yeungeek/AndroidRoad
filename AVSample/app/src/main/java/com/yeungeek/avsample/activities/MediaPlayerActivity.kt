package com.yeungeek.avsample.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yeungeek.avsample.databinding.ActivityMediaPlayerBinding

class MediaPlayerActivity : AppCompatActivity(), SurfaceHolder.Callback, View.OnClickListener {
    private lateinit var binding: ActivityMediaPlayerBinding
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var mediaPlayer: MediaPlayer
    private var isStop: Boolean = true

    /**
     * 1. create media player
     * 2. set datasource
     * 3. prepare/prepareAysnc
     * 4. start
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        surfaceHolder = binding.mediaPlayerSurface.holder
        surfaceHolder.addCallback(this)

        mediaPlayer = MediaPlayer()

        mediaPlayer.setDataSource("http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8")
        mediaPlayer.setOnPreparedListener {
            Log.d("DEBUG", "##### prepared ")
            mediaPlayer.start()
        }

        binding.mediaPlayerPauseBtn.setOnClickListener(this)
        binding.mediaPlayerStartBtn.setOnClickListener(this)
        binding.mediaPlayerStopBtn.setOnClickListener(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        var surface = holder.surface
        mediaPlayer.setSurface(surface)
        mediaPlayer.prepareAsync()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.mediaPlayerPauseBtn.id -> {
                mediaPlayer.pause()
                isStop = false
            }

            binding.mediaPlayerStartBtn.id -> {
                Log.d("DEBUG", "##### media player status: $isStop")
                if (isStop)
                    mediaPlayer.prepareAsync()
                else
                    mediaPlayer.start()

                isStop = false
            }
            binding.mediaPlayerStopBtn.id -> {
                mediaPlayer.stop()
                isStop = true
            }
        }
    }

    private fun release() {
        if (null != mediaPlayer && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}