package com.yeungeek.avsample.activities.opengl.book.data

import android.opengl.GLES20.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VertexArray constructor(vertexData: FloatArray) {
    private val floatBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexData.size * Cst.BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)

    fun setVertexAttribPointer(
        dataOffset: Int,
        attributeLocation: Int,
        componentCount: Int,
        stride: Int
    ) {
        floatBuffer.position(dataOffset)
        glVertexAttribPointer(
            attributeLocation,
            componentCount,
            GL_FLOAT,
            false,
            stride,
            floatBuffer
        )

        glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)
    }
}