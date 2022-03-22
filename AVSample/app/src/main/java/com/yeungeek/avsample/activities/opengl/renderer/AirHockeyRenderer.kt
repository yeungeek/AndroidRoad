package com.yeungeek.avsample.activities.opengl.renderer

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.yeungeek.avsample.activities.opengl.helper.ShaderHelper
import com.yeungeek.avsample.activities.opengl.helper.ShaderResReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRenderer constructor(context: Context) : GLSurfaceView.Renderer {

    private var mContext: Context = context

    private val A_POSITION = "a_Position"
    private val U_COLOR = "u_Color"
    private val POSITION_COMPONENT_COUNT = 2
    private val BYTES_PER_FLOAT = 4

    private var program = 0
    private var aPositionLocation = 0
    private var uColorLocation = 0

    private var vertexData: FloatBuffer? = null


    init {
        var tableVerticesWithTriangles = floatArrayOf(
            //Triangle 1
            -0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f,

            //Triangle 2
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,

            //Line
            -0.5f, 0f,
            0.5f, 0f,

            //Mallets
            0f, -0.25f,
            0f, 0.25f
        )

        vertexData = ByteBuffer
            .allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(tableVerticesWithTriangles)

//        vertexData
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)

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

        //5. Bind our data
        vertexData!!.position(0)

        GLES20.glVertexAttribPointer(
            aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, 0, vertexData
        )

        GLES20.glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Draw the table.
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        // Draw the center dividing line.
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        // Draw the first mallet blue.
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        // Draw the second mallet red.
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
    }
}