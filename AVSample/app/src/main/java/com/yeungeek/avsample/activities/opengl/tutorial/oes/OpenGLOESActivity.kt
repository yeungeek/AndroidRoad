package com.yeungeek.avsample.activities.opengl.tutorial.oes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yeungeek.avsample.activities.opengl.tutorial.oes.widget.Camera2GLSurfaceView

class OpenGLOESActivity : AppCompatActivity() {
    private lateinit var mGLSurfaceView: Camera2GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGLSurfaceView = Camera2GLSurfaceView(this)
        //renderer

        setContentView(mGLSurfaceView)
    }
}