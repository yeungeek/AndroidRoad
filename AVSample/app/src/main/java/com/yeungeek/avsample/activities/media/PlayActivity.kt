package com.yeungeek.avsample.activities.media

import android.media.MediaExtractor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yeungeek.avsample.R

class PlayActivity : AppCompatActivity() {
    private lateinit var mExtractor: MediaExtractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        init()
    }

    /**
     * 1. media extract
     */
    private fun init() {
        mExtractor = MediaExtractor()


    }
}