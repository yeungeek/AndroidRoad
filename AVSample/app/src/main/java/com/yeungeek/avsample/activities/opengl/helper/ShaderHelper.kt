package com.yeungeek.avsample.activities.opengl.helper

import android.opengl.GLES20
import android.util.Log
import timber.log.Timber

object ShaderHelper {
    /**
     * vertex shader
     */
    fun compileVertexShader(shaderCode: String): Int {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode)
    }

    /**
     * fragment shader
     */
    fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)
    }

    /**
     * link program
     */
    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        //1. Create a new program object
        val programObjectId = GLES20.glCreateProgram()
        if (programObjectId == 0) {
            return 0;
        }

        //2. Attach shader to program
        GLES20.glAttachShader(programObjectId, vertexShaderId)
        GLES20.glAttachShader(programObjectId, fragmentShaderId)

        //3. Link the two shaders together into a program.
        GLES20.glLinkProgram(programObjectId)

        //4. Get the link status
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0)
        Timber.d("##### link result: %s", GLES20.glGetProgramInfoLog(programObjectId))

        //5. Verify the link status.
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programObjectId)
            Timber.e("##### link error")
            return 0
        }

        return programObjectId
    }

    fun validateProgram(programObjectId: Int): Boolean {
        GLES20.glValidateProgram(programObjectId)
        val validateStatus = IntArray(1)
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)
        Timber.d("##### validate status: %s", GLES20.glGetProgramInfoLog(programObjectId))

        return validateStatus[0] != 0
    }

    private fun compileShader(type: Int, shaderCode: String): Int {
        //1. Create a new shader
        val shaderObjectId = GLES20.glCreateShader(type)
        if (0 == shaderObjectId) {
            Timber.e("##### error: compile shader")
            return 0
        }

        //2. Pass in the shader source
        GLES20.glShaderSource(shaderObjectId, shaderCode)

        //3. Compile the shader
        GLES20.glCompileShader(shaderObjectId)

        //4. Get the compilation status
        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        Timber.e(
            "##### compile result: %s, %s",
            shaderCode,
            GLES20.glGetShaderInfoLog(shaderObjectId)
        )

        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderObjectId)
            Timber.e("##### compile shader error")
            return 0
        }

        return shaderObjectId
    }
}