package com.yeungeek.videosample.opengl.base

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

open class BaseGLSurfaceView : GLSurfaceView {
    constructor(context: Context?) : super(context) {
        initGLContext()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initGLContext()
    }

    private fun initGLContext() {
        setEGLContextClientVersion(2)
    }
}