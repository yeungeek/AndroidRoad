package com.yeungeek.avsample.activities.opengl.book.objects

import android.opengl.GLES20.*
import com.yeungeek.avsample.activities.opengl.book.data.Cst.BYTES_PER_FLOAT
import com.yeungeek.avsample.activities.opengl.book.data.VertexArray
import com.yeungeek.avsample.activities.opengl.book.programs.ColorShaderProgram

class Mallet {
    private val POSITION_COMPONENT_COUNT = 2
    private val COLOR_COMPONENT_COUNT = 3
    private val STRIDE: Int = ((POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)
            * BYTES_PER_FLOAT)
    private val VERTEX_DATA = floatArrayOf(
        // Order of coordinates: X, Y, R, G, B
        0f, -0.4f, 0f, 0f, 1f,
        0f, 0.4f, 1f, 0f, 0f
    )

    private val vertexArray: VertexArray = VertexArray(VERTEX_DATA)

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            STRIDE
        )

        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            colorProgram.getColorAttributeLocation(),
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw() {
        glDrawArrays(GL_POINTS, 0, 2)
    }
}