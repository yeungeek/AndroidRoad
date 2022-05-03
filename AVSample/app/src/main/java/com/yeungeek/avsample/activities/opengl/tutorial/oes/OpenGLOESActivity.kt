package com.yeungeek.avsample.activities.opengl.tutorial.oes

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class OpenGLOESActivity : AppCompatActivity() {
    private lateinit var mGLSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGLSurfaceView = GLSurfaceView(this)
        mGLSurfaceView.setEGLContextClientVersion(3)
        //renderer

        setContentView(mGLSurfaceView)
    }
}