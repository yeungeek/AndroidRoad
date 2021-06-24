package com.yeungeek.videosample.av

import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.yeungeek.videosample.R

class VideoRecorderActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var textureView: TextureView
    private lateinit var startBtn: Button
    private lateinit var stopBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_recorder)
        initViews()
    }

    fun initViews() {
        startBtn = findViewById(R.id.start_btn)
        startBtn.setOnClickListener(this)

        stopBtn = findViewById(R.id.stop_btn)
        stopBtn.setOnClickListener(this)

        textureView = findViewById(R.id.texture_view)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.start_btn -> {

            }

            R.id.stop_btn -> {

            }
        }
    }
}