package com.yeungeek.videosample.opengl.shape.triangle

import android.opengl.GLES20
import android.util.Log
import com.yeungeek.videosample.opengl.base.BaseGLSL
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

open class Triangle : BaseGLSL() {
    // 简单的顶点着色器
    open val vertexShaderCode =
        "attribute vec4 vPosition;\n" +
                " void main() {\n" +
                "     gl_Position   = vPosition;\n" +
                " }";

    // 简单的片元着色器
    open val fragmentShaderCode =
        "precision mediump float;\n" +
                " uniform vec4 vColor;\n" +
                " void main() {\n" +
                "     gl_FragColor = vColor;\n" +
                " }";

    // 定义三角形的坐标
    var triangleCoords = floatArrayOf(
        0.0f, 0.5f, 0.0f,  // top
        -0.5f, -0.5f, 0.0f,  // bottom left
        0.5f, -0.5f, 0.0f // bottom right
    )

    // 定义三角形的颜色——白色
    var color = floatArrayOf(0.5f, 0.5f, 1.0f, 1.0f)

    // 顶点个数
    val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX

    var vertexBuffer: FloatBuffer
    var mProgram = 0

    init {
        Log.d("DEBUG","##### Triangle init method")
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        val buffer = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        buffer.order(ByteOrder.nativeOrder())

        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        vertexBuffer = buffer.asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)

        mProgram = createOpenGLProgram(vertexShaderCode, fragmentShaderCode)
    }

    open fun draw() {
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram)
        //获取顶点着色器的vPosition成员句柄
        val mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(
            mPositionHandle,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )
        //获取片元着色器的vColor成员的句柄
        val mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }
}