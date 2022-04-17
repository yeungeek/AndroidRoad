package com.yeungeek.avsample.activities.opengl.book.programs

import android.content.Context
import android.opengl.GLES20.*

class ColorShaderProgram(
    context: Context, vertexShaderFile: String,
    fragmentShaderFile: String
) : ShaderProgram(context, vertexShaderFile, fragmentShaderFile) {
    // Uniform locations
    private var uMatrixLocation = 0

    // Attribute locations
    private var aPositionLocation = 0
    private var aColorLocation = 0

    constructor(context: Context) : this(
        context, "airhockey/simple_vertex_shader.glsl",
        "airhockey/simple_fragment_shader.glsl"
    ) {
        this.uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aColorLocation = glGetAttribLocation(program, A_COLOR)
    }

    fun setUniforms(matrix: FloatArray?) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    fun getPositionAttributeLocation() = aPositionLocation

    fun getColorAttributeLocation() = aColorLocation
}