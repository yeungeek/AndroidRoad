package com.yeungeek.videosample.opengl.shape.triangle

import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder

open class CameraTriangle : Triangle() {
    // 支持矩阵变换的顶点着色器
    open val vertexMatrixShaderCode =
        "attribute vec4 vPosition;\n" +
                "uniform mat4 vMatrix;\n" +
                "void main() {\n" +
                "    gl_Position = vMatrix * vPosition;\n" +
                "}"

    private val mViewMatrix = FloatArray(16)
    private val mProjectMatrix = FloatArray(16)
    private val mMVPMatrix = FloatArray(16)

    private var mMatrixHandler = 0
    private var mPositionHandle = 0
    private var mColorHandle = 0

    init {
        Log.d("DEBUG", "##### CameraTriangle init method")

        val buffer = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        buffer.order(ByteOrder.nativeOrder())

        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        vertexBuffer = buffer.asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)

        mProgram = createOpenGLProgram(vertexMatrixShaderCode, fragmentShaderCode)
    }


    fun onSurfaceChanged(width: Int, height: Int) {
        //计算宽高比
        val ratio = width.toFloat() / height

        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 7.0f)
        //设置相机位置
        Matrix.setLookAtM(
            mViewMatrix, 0, 0f, 0f, 7.0f, 0f,
            0f, 0f, 0f, 1.0f, 0f
        )
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun draw() {
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram)
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix")
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0)
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
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
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }
}