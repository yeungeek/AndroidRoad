package com.yeungeek.videosample.opengl.base

import android.opengl.GLES20
import android.util.Log


abstract class BaseGLSL {
    private val TAG = "BaseGLSL"

    open val COORDS_PER_VERTEX = 3 // 每个顶点的坐标数

    val vertexStride = COORDS_PER_VERTEX * 4 // 每个顶点四个字节

    /**
     * 加载着色器
     *
     * @param type       加载着色器类型
     * @param shaderCode 加载着色器的代码
     */
    open fun loadShader(type: Int, shaderCode: String): Int {
        //根据type创建顶点着色器或者片元着色器
        val shader = GLES20.glCreateShader(type)
        //将着色器的代码加入到着色器中
        GLES20.glShaderSource(shader, shaderCode)
        //编译着色器
        GLES20.glCompileShader(shader)
        return shader
    }

    /**
     * 生成OpenGL Program
     *
     * @param vertexSource   顶点着色器代码
     * @param fragmentSource 片元着色器代码
     * @return 生成的OpenGL Program，如果为0，则表示创建失败
     */
    open fun createOpenGLProgram(vertexSource: String, fragmentSource: String): Int {
        val vertex = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        if (vertex == 0) {
            Log.e(TAG, "loadShader vertex failed")
            return 0
        }
        val fragment = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
        if (fragment == 0) {
            Log.e(TAG, "loadShader fragment failed")
            return 0
        }
        var program = GLES20.glCreateProgram()
        if (program != 0) {
            GLES20.glAttachShader(program, vertex)
            GLES20.glAttachShader(program, fragment)
            GLES20.glLinkProgram(program)
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program:" + GLES20.glGetProgramInfoLog(program))
                GLES20.glDeleteProgram(program)
                program = 0
            }
        }
        return program
    }
}