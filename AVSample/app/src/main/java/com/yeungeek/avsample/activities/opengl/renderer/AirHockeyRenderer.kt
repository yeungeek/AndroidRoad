package com.yeungeek.avsample.activities.opengl.renderer

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.yeungeek.avsample.activities.opengl.helper.ShaderHelper
import com.yeungeek.avsample.activities.opengl.helper.ShaderResReader
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRenderer constructor(context: Context) : GLSurfaceView.Renderer {

    private var mContext: Context = context

    private val A_POSITION = "a_Position"
    private val U_MATRIX = "u_Matrix"

    //    private val U_COLOR = "u_Color"
    private val A_COLOR = "a_Color"
    private val POSITION_COMPONENT_COUNT = 2
    private val COLOR_COMPONENT_COUNT = 3
    private val BYTES_PER_FLOAT = 4
    private val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
    private val projectionMatrix = FloatArray(16)

    private var program = 0
    private var aPositionLocation = 0

    //private var uColorLocation = 0
    private var aColorLocation = 0
    private var uMatrixLocation = 0

    private var vertexData: FloatBuffer? = null

    init {
        var tableVerticesWithTriangles = floatArrayOf(
            //Triangle Fan  X,Y,R,G,B
            0f, 0f, 1.0f, 1.0f, 1.0f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

            // Line
            -0.5f, 0f, 1.0f, 0f, 0f,
            0.5f, 0f, 1.0f, 0f, 0f,

            //Mallets
            0f, -0.25f, 0f, 0f, 1f,
            0f, 0.25f, 1f, 0f, 0f

//            //Triangle 1,
//            -0.5f, -0.5f,
//            0.5f, 0.5f,
//            -0.5f, 0.5f,
//
//            //Triangle 2
//            -0.5f, -0.5f,
//            0.5f, -0.5f,
//            0.5f, 0.5f,
//
//            //Line
//            -0.5f, 0f,
//            0.5f, 0f,
//
//            //Mallets
//            0f, -0.25f,
//            0f, 0.25f
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

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)

        Timber.d("##### aColorLocation: %d", aColorLocation)

        //5. Bind our data
        vertexData!!.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, STRIDE, vertexData
        )

//        GLES20.glVertexAttribPointer(
//            aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
//            false, 0, vertexData
//        )

        GLES20.glEnableVertexAttribArray(aPositionLocation)

        //2. add color location
        vertexData!!.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(
            aColorLocation,
            COLOR_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )

        GLES20.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val aspectRatio = when (width > height) {
            true -> width.toFloat() / height.toFloat()
            false -> height.toFloat() / width.toFloat()
        }

        // ortho
        if (width > height) {
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)
        // Draw the table
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)

        // Draw the center dividing line.
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)


        // Draw the first mallet blue.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        // Draw the second mallet red.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);


//        // Draw the table.
//        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
//
//        // Draw the center dividing line.
//        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
//        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);
//
//        // Draw the first mallet blue.
//        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
//        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);
//
//        // Draw the second mallet red.
//        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
//        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
    }
}