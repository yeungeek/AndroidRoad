package com.yeungeek.avsample.activities.opengl.airhockey

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yeungeek.avsample.activities.opengl.renderer.AirHTextureRenderer

class AirHTextureActivity : AppCompatActivity() {
    lateinit var glSurfaceView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glSurfaceView = GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(AirHTextureRenderer(applicationContext))

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