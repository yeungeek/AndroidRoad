package com.yeungeek.avsample.activities.opengl.tutorial.oes.widget

import android.app.Activity
import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.yeungeek.avsample.activities.opengl.tutorial.oes.camera.Camera2Manager
import com.yeungeek.avsample.activities.opengl.tutorial.oes.objects.Camera2Obj
import timber.log.Timber
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Camera2GLSurfaceView : GLSurfaceView,
    GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    private lateinit var mCamera2Manager: Camera2Manager
    private lateinit var mSurfaceTexture: SurfaceTexture
    private var mTextureId = 0
    private lateinit var mCameraObj: Camera2Obj

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        mCamera2Manager = Camera2Manager(context as Activity)
        setEGLContextClientVersion(3)
        setRenderer(this)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mTextureId = loadTexture()

        Timber.d("##### load texture: $mTextureId")

        mSurfaceTexture = SurfaceTexture(mTextureId)
        mSurfaceTexture.setOnFrameAvailableListener(this)
        mCamera2Manager.openCamera(width, height)
        mCamera2Manager.setPreviewSurface(mSurfaceTexture)

        mCameraObj = Camera2Obj(context, mSurfaceTexture, mTextureId)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        mCameraObj.draw()
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        requestRender()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mCamera2Manager.closeCamera()
    }
    //GL Method (OES Texture)
    private fun loadTexture(): Int {
        val texture = intArrayOf(1)
        //1. create texture
        GLES30.glGenTextures(1, texture, 0)
        //2. bind texture oes
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0])
        //3. params
        GLES30.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_NEAREST.toFloat()
        )
        GLES30.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR.toFloat()
        )
        GLES30.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )
        GLES30.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_CLAMP_TO_EDGE.toFloat()
        )

        //4. clear
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
        return texture[0]
    }
}