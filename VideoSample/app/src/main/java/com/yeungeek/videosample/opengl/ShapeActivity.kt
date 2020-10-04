package com.yeungeek.videosample.opengl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yeungeek.videosample.opengl.surface.TriangleGLSurfaceView

class ShapeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(TriangleGLSurfaceView(this))
    }
}