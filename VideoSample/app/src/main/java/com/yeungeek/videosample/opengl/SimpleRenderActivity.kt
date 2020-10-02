package com.yeungeek.videosample.opengl

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SimpleRenderActivity : AppCompatActivity() {
    private lateinit var mGLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGLView = SimpleGLSurfaceView(this)
        setContentView(mGLView)
    }

}