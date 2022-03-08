package com.yeungeek.avsample.activities.opengl.renderer

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.yeungeek.avsample.activities.opengl.helper.ShaderHelper
import com.yeungeek.avsample.activities.opengl.helper.ShaderResReader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRenderer constructor(context: Context) : GLSurfaceView.Renderer {
    private var mContext: Context = context

    private val A_POSITION = "a_Position"
    private val U_COLOR = "u_Color"

    private var program: Int = 0
    private var aPositionLocation: Int = 0
    private var uColorLocation: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(1f, 0f, 0f, 0f)

        //1. load source
        val vertexShaderSource = ShaderResReader.loadFromAssetsFile(
            "airhockey/simple_vertex_shader.glsl",
            mContext?.resources
        )

        val fragmentShaderSource = ShaderResReader.loadFromAssetsFile(
            "airhockey/simple_fragment_shader.glsl",
            mContext?.resources
        )

        //2. compile shader
        val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource)
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)

        //3. link program
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)

        //4. validate program and use program
        ShaderHelper.validateProgram(program)
        GLES20.glUseProgram(program)

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }
}