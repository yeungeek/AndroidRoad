package com.yeungeek.avsample.activities.opengl.tutorial.oes.camera

import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Size
import java.util.*
import kotlin.Comparator


class Camera2Manager constructor(activity: Activity) {
    private val mContext: Context
    private val mCameraManager: CameraManager by lazy {
        activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    private lateinit var mCameraId: String
    private lateinit var mPreviewSize: Size

    init {
        mContext = activity
    }

    fun openCamera(width: Int, height: Int) {
        setupCamera(width, height)
    }

    private fun setupCamera(width: Int, height: Int) {
        for (cameraId in mCameraManager.cameraIdList) {
            val characteristics = mCameraManager.getCameraCharacteristics(cameraId)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                continue
            }

            val map =
                characteristics[CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP] ?: continue

            val largest = Collections.max(
                map.getOutputSizes(ImageFormat.JPEG).toList(),
                CompareSizesByArea()
            )

            //image reader

//            mPreviewSize =

            mCameraId = cameraId
            return
        }
    }

    internal class CompareSizesByArea : Comparator<Size> {
        override fun compare(lhs: Size, rhs: Size): Int {
            return java.lang.Long.signum(
                lhs.width.toLong() * lhs.height -
                        rhs.width.toLong() * rhs.height
            )
        }
    }
}