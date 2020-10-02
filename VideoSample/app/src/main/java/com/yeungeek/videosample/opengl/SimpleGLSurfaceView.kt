package com.yeungeek.videosample.opengl

import android.content.Context
import android.opengl.GLSurfaceView

class SimpleGLSurfaceView : GLSurfaceView {
    var mRenderer: SimpleRender

    constructor(context: Context) : super(context) {
        //OpenGL ES 2.0
        setEGLContextClientVersion(2)

        //render
        mRenderer = SimpleRender()

        //set the render
        setRenderer(mRenderer)
    }
}