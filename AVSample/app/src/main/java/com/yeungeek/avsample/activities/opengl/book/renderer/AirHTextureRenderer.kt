package com.yeungeek.avsample.activities.opengl.book.renderer

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.yeungeek.avsample.R
import com.yeungeek.avsample.activities.opengl.helper.TextureHelper
import com.yeungeek.avsample.activities.opengl.book.objects.Mallet
import com.yeungeek.avsample.activities.opengl.book.objects.Table
import com.yeungeek.avsample.activities.opengl.book.programs.ColorShaderProgram
import com.yeungeek.avsample.activities.opengl.book.programs.TextureShaderProgram
import timber.log.Timber
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHTextureRenderer constructor(context: Context) : GLSurfaceView.Renderer {
    private val mContext: Context = context
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    private lateinit var table: Table
    private lateinit var mallet: Mallet
    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram
    private var texture = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 0f)

        table = Table()
        mallet = Mallet()

        textureProgram = TextureShaderProgram(mContext)
        colorProgram = ColorShaderProgram(mContext)

        texture = TextureHelper.loadTexture(mContext, R.mipmap.air_hockey_surface)
        Timber.d("##### Load texture %d", texture)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        Matrix.perspectiveM(
            projectionMatrix, 0, 45f, width.toFloat() / height.toFloat(),
            1f, 10f
        )

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -3.5f)
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f)

        val temp = FloatArray(16)
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        textureProgram.useProgram()
        textureProgram.setUniforms(projectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        colorProgram.useProgram()
        colorProgram.setUniforms(projectionMatrix)
        mallet.bindData(colorProgram)
        mallet.draw()
    }
}