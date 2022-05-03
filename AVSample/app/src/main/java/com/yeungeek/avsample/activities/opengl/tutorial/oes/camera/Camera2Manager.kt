package com.yeungeek.avsample.activities.opengl.tutorial.oes.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.Point
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import android.view.SurfaceHolder
import timber.log.Timber
import java.util.*


class Camera2Manager constructor(activity: Activity) {
    private val mActivity: Activity = activity
    private val mCameraManager: CameraManager by lazy {
        activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    private lateinit var mCameraId: String
    private lateinit var mPreviewSize: Size
    private var mBackgroundThread: HandlerThread? = null

    /**
     * A [Handler] for running tasks in the background.
     */
    private var mBackgroundHandler: Handler? = null

    private var mCameraDevice: CameraDevice? = null

    private lateinit var mPreviewSurface: Surface

    private val MAX_PREVIEW_WIDTH = 1920

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private val MAX_PREVIEW_HEIGHT = 1080

    private var mPreviewRequestBuilder: CaptureRequest.Builder? = null
    private var mCaptureSession: CameraCaptureSession? = null
    private lateinit var mPreviewRequest: CaptureRequest

    private var mImageReader: ImageReader? = null

    @SuppressLint("MissingPermission")
    fun openCamera(width: Int, height: Int) {
        startBackgroundThread()

        setupCameraOutputs(width, height)

        //open camera
        mCameraManager.openCamera(mCameraId, mStateCallback, mBackgroundHandler)
    }

    fun closeCamera() {
        mCaptureSession?.close()
        mCaptureSession = null
        mCameraDevice?.close()
        mCameraDevice = null

        stopBackgroundThread()
    }

    fun setPreviewSurface(surfaceTexture: SurfaceTexture) {
        //after open camera
        surfaceTexture.setDefaultBufferSize(mPreviewSize?.width, mPreviewSize?.height)
        mPreviewSurface = Surface(surfaceTexture)
    }

    fun setPreviewSurface(holder: SurfaceHolder) {
        mPreviewSurface = holder.surface
    }

    private fun setupCameraOutputs(width: Int, height: Int) {
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

            mImageReader =
                ImageReader.newInstance(
                    largest.width,
                    largest.height,
                    ImageFormat.JPEG,
                    2
                ).apply {
                    setOnImageAvailableListener(
                        mOnImageAvailableListener,
                        mBackgroundHandler
                    )
                }

            //image reader
            var displayRotation = mActivity.windowManager.defaultDisplay.rotation
            var swappedDimensions = false

            var sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)

            when (displayRotation) {
                Surface.ROTATION_0, Surface.ROTATION_180 -> {
                    if (sensorOrientation == 90 || sensorOrientation == 270) {
                        swappedDimensions = true
                    }
                }

                Surface.ROTATION_90, Surface.ROTATION_270 -> {
                    if (sensorOrientation == 0 || sensorOrientation == 180) {
                        swappedDimensions = true
                    }
                }
                else -> Timber.e("###### rotation is invalid")
            }

            var displaySize = Point()
            mActivity.windowManager.defaultDisplay.getSize(displaySize)
            var rotatedPreviewWidth = width
            var rotatedPreviewHeight = height
            var maxPreviewWidth = displaySize.x
            var maxPreviewHeight = displaySize.y

            if (swappedDimensions) {
                rotatedPreviewWidth = height
                rotatedPreviewHeight = width
                maxPreviewWidth = displaySize.y
                maxPreviewHeight = displaySize.x
            }

            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                maxPreviewWidth = MAX_PREVIEW_WIDTH
            }

            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                maxPreviewHeight = MAX_PREVIEW_HEIGHT
            }

            mPreviewSize = chooseOptimalSize(
                map.getOutputSizes(SurfaceTexture::class.java),
                rotatedPreviewWidth,
                rotatedPreviewHeight,
                maxPreviewWidth,
                maxPreviewHeight,
                largest
            )

            mCameraId = cameraId
            return
        }
    }

    private fun createCameraPreviewSession() {
        mPreviewRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        mPreviewRequestBuilder?.addTarget(mPreviewSurface)

        mCameraDevice?.createCaptureSession(
            listOf(mPreviewSurface, mImageReader?.surface),
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    mCaptureSession = session

                    //auto
                    mPreviewRequestBuilder?.set(
                        CaptureRequest.CONTROL_AF_MODE,
                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                    )
                    //auto flash
                    mPreviewRequestBuilder?.set(
                        CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                    )

                    mPreviewRequest = mPreviewRequestBuilder!!.build()
                    mCaptureSession?.setRepeatingRequest(mPreviewRequest, null, mBackgroundHandler)
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Timber.e("##### ConfigureFailed")
                }

            },
            mBackgroundHandler
        )
    }

    private fun chooseOptimalSize(
        choices: Array<Size>, textureViewWidth: Int,
        textureViewHeight: Int, maxWidth: Int, maxHeight: Int, aspectRatio: Size
    ): Size {

        // Collect the supported resolutions that are at least as big as the preview Surface
        val bigEnough: MutableList<Size> = ArrayList()
        // Collect the supported resolutions that are smaller than the preview Surface
        val notBigEnough: MutableList<Size> = ArrayList()
        val w = aspectRatio.width
        val h = aspectRatio.height
        for (option in choices) {
            if (option.width <= maxWidth && option.height <= maxHeight && option.height == option.width * h / w) {
                if (option.width >= textureViewWidth &&
                    option.height >= textureViewHeight
                ) {
                    bigEnough.add(option)
                } else {
                    notBigEnough.add(option)
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        return if (bigEnough.size > 0) {
            Collections.min(bigEnough, CompareSizesByArea())
        } else if (notBigEnough.size > 0) {
            Collections.max(notBigEnough, CompareSizesByArea())
        } else {
            Timber.e("##### Couldn't find any suitable preview size")
            choices[0]
        }
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("CameraBackground")
        mBackgroundThread!!.start()
        mBackgroundHandler = Handler(mBackgroundThread!!.looper)
    }

    private fun stopBackgroundThread() {
        mBackgroundThread?.quitSafely()

        try {
            mBackgroundThread?.join()
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    //callback
    private val mStateCallback: CameraDevice.StateCallback =
        object : CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice) {
                mCameraDevice = cameraDevice
                createCameraPreviewSession()
            }

            override fun onDisconnected(cameraDevice: CameraDevice) {
            }

            override fun onError(cameraDevice: CameraDevice, error: Int) {
            }
        }

    private val mOnImageAvailableListener = ImageReader.OnImageAvailableListener {

    }

    /**
     * CompareSizesByArea
     */
    internal class CompareSizesByArea : Comparator<Size> {
        override fun compare(lhs: Size, rhs: Size): Int {
            return java.lang.Long.signum(
                lhs.width.toLong() * lhs.height -
                        rhs.width.toLong() * rhs.height
            )
        }
    }
}

