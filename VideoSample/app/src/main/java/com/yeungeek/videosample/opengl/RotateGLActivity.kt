package com.yeungeek.videosample.opengl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yeungeek.videosample.opengl.surface.RotateTriangleGLSurfaceView

class RotateGLActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(RotateTriangleGLSurfaceView(this))
    }
}