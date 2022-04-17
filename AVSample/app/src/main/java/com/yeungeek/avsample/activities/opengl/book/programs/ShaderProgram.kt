package com.yeungeek.avsample.activities.opengl.book.programs

import android.content.Context
import android.opengl.GLES20
import com.yeungeek.avsample.activities.opengl.book.helper.ShaderHelper
import com.yeungeek.avsample.activities.opengl.book.helper.ShaderResReader

abstract class ShaderProgram {
    // Uniform constants
    protected val U_MATRIX = "u_Matrix"
    protected val U_TEXTURE_UNIT = "u_TextureUnit"

    // Attribute constants
    protected val A_POSITION = "a_Position"
    protected val A_COLOR = "a_Color"
    protected val A_TEXTURE_COORDINATES = "a_TextureCoordinates"

    protected val program: Int

    constructor(
        context: Context,
        vertexShaderFile: String,
        fragmentShaderFile: String
    ) {
        val vertexShaderSource = ShaderResReader.loadFromAssetsFile(
            vertexShaderFile,
            context.resources
        )

        val fragmentShaderSource = ShaderResReader.loadFromAssetsFile(
            fragmentShaderFile,
            context.resources
        )

        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource)
    }

    fun useProgram() {
        GLES20.glUseProgram(program)
    }
}