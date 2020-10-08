package com.yeungeek.videosample.opengl.surface

import android.content.Context
import android.opengl.GLES20
import android.util.Log
import com.yeungeek.videosample.opengl.base.BaseGLSurfaceView
import com.yeungeek.videosample.opengl.shape.triangle.CameraTriangle
import com.yeungeek.videosample.opengl.shape.triangle.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TriangleGLSurfaceView : BaseGLSurfaceView {
    constructor(context: Context?) : super(context) {
//        setRenderer(TriangleRenderer())
        setRenderer(CameraTriangleRenderer())
    }

    class TriangleRenderer : Renderer {
        lateinit var triangle: Triangle
        override fun onDrawFrame(gl: GL10?) {
            triangle.draw()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            Log.d("DEBUG", "##### TriangleGLSurfaceView onSurfaceCreated")
            triangle = Triangle()
        }
    }

    class CameraTriangleRenderer : Renderer {
        lateinit var triangle: CameraTriangle

        override fun onDrawFrame(p0: GL10?) {
            triangle.draw()
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            triangle.onSurfaceChanged(width, height)
        }

        override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
            triangle = CameraTriangle()
        }
    }


}