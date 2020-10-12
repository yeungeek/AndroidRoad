package com.yeungeek.videosample.opengl.surface

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import android.view.MotionEvent
import com.yeungeek.videosample.opengl.base.BaseGLSurfaceView
import com.yeungeek.videosample.opengl.shape.triangle.RotateTriangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class RotateTriangleGLSurfaceView(context: Context?) : BaseGLSurfaceView(context) {
    private val mRenderer: RotateTriangleRenderer
    private val TOUCH_SCALE_FACTOR = 180.0f / 320
    var mPreviousX = 0.0f
    var mPreviousY = 0.0f

    init {
        mRenderer = RotateTriangleRenderer()
        setRenderer(mRenderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x as Float
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx = x!! - mPreviousX
                var dy = y!! - mPreviousY

                if (y > height / 2) {
                    dx = dx * -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    dy = dy * -1
                }

                mRenderer.mAngle = mRenderer.mAngle + (dx + dy) * TOUCH_SCALE_FACTOR
                requestRender()
            }
            else -> {

            }
        }

        mPreviousX = x
        mPreviousY = y

        return true
    }

    class RotateTriangleRenderer : Renderer {
        private lateinit var mTriangle: RotateTriangle
        var mAngle = 0.0f

        // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
        private val mMVPMatrix = FloatArray(16)
        private val mProjectionMatrix = FloatArray(16)
        private val mViewMatrix = FloatArray(16)
        private val mRotationMatrix = FloatArray(16)

        override fun onDrawFrame(p0: GL10?) {
            val scratch = FloatArray(16)


            // Draw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

            // Set the camera position (View matrix)
            Matrix.setLookAtM(
                mViewMatrix,
                0,
                0f,
                0f,
                -3f,
                0f,
                0f,
                0f,
                0f,
                1.0f,
                0.0f
            )

            // Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)


            // Create a rotation for the triangle

            // Use the following code to generate constant rotation.
            // Leave this code out when using TouchEvents.
            // long time = SystemClock.uptimeMillis() % 4000L;
            // float angle = 0.090f * ((int) time);
            Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0f, 0f, 1.0f)


            // Combine the rotation matrix with the projection and camera view
            // Note that the mMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0)


            // Draw triangle
            mTriangle.draw(scratch)
        }

        override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)

            var ratio = width * 1.0f / height
            Log.d("DEBUG", "##### ration: $ratio")

            // this projection matrix is applied to object coordinates
            // in the onDrawFrame() method
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 7.0f)
        }

        override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

            mTriangle = RotateTriangle()
        }
    }
}