package com.yeungeek.avsample.activities.media

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.yeungeek.avsample.databinding.ActivityMediaCodecBinding

class MediaCodecActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaCodecBinding
    private val SDCARD_PATH = "${Environment.getExternalStorageDirectory().absolutePath}"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaCodecBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}