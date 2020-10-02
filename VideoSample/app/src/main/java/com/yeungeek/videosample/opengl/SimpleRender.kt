package com.yeungeek.videosample.opengl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SimpleRender : GLSurfaceView.Renderer {
    override fun onDrawFrame(p0: GL10?) {
        // redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }

    override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
        GLES20.glViewport(0, 0, p1, p2)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        //set background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    }
}