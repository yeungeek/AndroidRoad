package com.yeungeek.avsample.activities.opengl.tutorial.oes.objects

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaRecorder
import android.opengl.GLES11Ext
import android.opengl.GLES30
import com.yeungeek.avsample.activities.opengl.helper.ShaderHelper
import com.yeungeek.avsample.activities.opengl.helper.ShaderResReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Camera2Obj {
    private val mVertexBuffer: FloatBuffer
    private val mTexVertexBuffer: FloatBuffer
    private val mVertexIndex: ShortBuffer

    private val POSITION_VERTEX = floatArrayOf(
        0f, 0f, 0f,
        1f, 1f, 1f,
        -1f, 1f, 0f,
        -1f, -1f, 0f,
        1f, -1f, 0f
    )

    private val TEX_VERTEX = floatArrayOf(
        0.5f, 0.5f,
        1f, 1f,
        0f, 1f,
        0f, 0f,
        1f, 0f
    )

    private val VERTEX_INDEX = shortArrayOf(
        0, 1, 2,
        0, 2, 3,
        0, 3, 4,
        0, 4, 1
    )

    private val mTextureId: Int
    private val mProgram: Int
    private val uTextureMatrixLocation: Int
    private val uTextureSamplerLocation: Int
    private val mSurfaceTexture: SurfaceTexture

    private var mTransformMatrix = FloatArray(16)

    constructor(context: Context, surfaceTexture: SurfaceTexture, textureId: Int) {
        mVertexBuffer =
            ByteBuffer.allocateDirect(POSITION_VERTEX.size * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(POSITION_VERTEX)
        mVertexBuffer.position(0)

        mTexVertexBuffer =
            ByteBuffer.allocateDirect(TEX_VERTEX.size * 4).order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEX_VERTEX)
        mTexVertexBuffer.position(0)

        mVertexIndex =
            ByteBuffer.allocateDirect(TEX_VERTEX.size * 4).order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(VERTEX_INDEX)
        mVertexIndex.position(0)

        val vertexShaderId = ShaderResReader.loadFromAssetsFile(
            "tutorial/vertex_camera_shader.glsl",
            context.resources
        )
        val fragmentShaderId = ShaderResReader.loadFromAssetsFile(
            "tutorial/fragment_camera_shader.glsl",
            context.resources
        )

        mProgram = ShaderHelper.buildProgram(vertexShaderId, fragmentShaderId)

        uTextureMatrixLocation = GLES30.glGetUniformLocation(mProgram, "uTextureMatrix")
        uTextureSamplerLocation = GLES30.glGetUniformLocation(mProgram, "yuvTexSampler")

        mSurfaceTexture = surfaceTexture
        mTextureId = textureId
    }

    fun draw() {
        GLES30.glUseProgram(mProgram)

        mSurfaceTexture.updateTexImage()
        mSurfaceTexture.getTransformMatrix(mTransformMatrix)

        //1. active texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)

        //2. bind texture
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId)
        //3.
        GLES30.glUniform1i(uTextureSamplerLocation, 0)

        //4.
        GLES30.glUniformMatrix4fv(uTextureMatrixLocation, 1, false, mTransformMatrix, 0)

        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, mVertexBuffer)

        //5.
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, mTexVertexBuffer)

        //draw
        GLES30.glDrawElements(
            GLES30.GL_TRIANGLES,
            VERTEX_INDEX.size,
            GLES30.GL_UNSIGNED_SHORT,
            mVertexIndex
        )
    }
}