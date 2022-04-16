package com.yeungeek.avsample.activities.opengl.book.objects

import com.yeungeek.avsample.activities.opengl.book.data.Cst
import com.yeungeek.avsample.activities.opengl.book.data.VertexArray

class Table {
    private val POSITION_COMPONENT_COUNT = 2
    private val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
    private val STRIDE: Int = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * Cst.BYTES_PER_FLOAT

    private val VERTEX_DATA = floatArrayOf(
        // Order of coordinates: X, Y, S, T
        // Triangle Fan
        0f, 0f, 0.5f, 0.5f,
        -0.5f, -0.8f, 0f, 0.9f,
        0.5f, -0.8f, 1f, 0.9f,
        0.5f, 0.8f, 1f, 0.1f,
        -0.5f, 0.8f, 0f, 0.1f,
        -0.5f, -0.8f, 0f, 0.9f
    )

    private val vertexArray: VertexArray = VertexArray(VERTEX_DATA)

    fun bindData(){

    }
}