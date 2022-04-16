package com.yeungeek.avsample.activities.opengl.book.first

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yeungeek.avsample.activities.opengl.book.renderer.FirstOpenGLRenderer

class FirstOpenGLActivity : AppCompatActivity() {
    lateinit var glSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)
        //config
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(FirstOpenGLRenderer())

        setContentView(glSurfaceView)
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView?.onResume()
    }
}