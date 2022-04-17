package com.yeungeek.avsample.activities.opengl.book.programs

import android.content.Context
import android.opengl.GLES20.*


class TextureShaderProgram(
    context: Context, vertexShaderFile: String,
    fragmentShaderFile: String
) : ShaderProgram(context, vertexShaderFile, fragmentShaderFile) {
    // Uniform locations
    private var uMatrixLocation = 0
    private var uTextureUnitLocation = 0

    // Attribute locations
    private var aPositionLocation = 0
    private var aTextureCoordinatesLocation = 0

    //    constructor() : this(null, "", "")
    constructor(context: Context) : this(
        context,
        "airhockey/texture_vertex_shader.glsl",
        "airhockey/texture_fragment_shader.glsl"
    ) {
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation =
            glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

        glActiveTexture(GL_TEXTURE0)

        glBindTexture(GL_TEXTURE_2D, textureId)

        glUniform1i(uTextureUnitLocation,0)
    }

    fun getPositionAttributeLocation() = aPositionLocation

    fun getTextureCoordinatesAttributeLocation() = aTextureCoordinatesLocation
}