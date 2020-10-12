package com.yeungeek.videosample.opengl.shape.triangle

import android.opengl.GLES20
import android.util.Log
import com.yeungeek.videosample.opengl.base.BaseGLSL
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

open class RotateTriangle : BaseGLSL() {
    private val vertexShaderCode = "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}"

    private val fragmentShaderCode = "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}"

    var vertexBuffer: FloatBuffer
    private var mProgram = 0
    private var mPositionHandle = 0
    private var mColorHandle = 0
    private var mMVPMatrixHandle = 0

    // number of coordinates per vertex in this array
    var triangleCoords = floatArrayOf( // in counterclockwise order:
        0.0f, 0.622008459f, 0.0f,  // top
        -0.5f, -0.311004243f, 0.0f,  // bottom left
        0.5f, -0.311004243f, 0.0f // bottom right
    )
    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
//    private override val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex


    var color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 0.0f)

    init {
        Log.d("DEBUG", "##### init Rotate Triangle")
        // initialize vertex byte buffer for shape coordinates
        val bb =
            ByteBuffer.allocateDirect(
                triangleCoords.size * 4
            )
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder())

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer()
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords)
        // set the buffer to read the first coordinate
        vertexBuffer.position(0)

        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode)
    }

    open fun draw(mvpMatrix: FloatArray) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle)

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
            mPositionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            vertexStride, vertexBuffer
        )

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }
}