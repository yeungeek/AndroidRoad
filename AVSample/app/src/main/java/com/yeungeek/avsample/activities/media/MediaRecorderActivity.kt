package com.yeungeek.avsample.activities.media

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.*
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yeungeek.avsample.R
import com.yeungeek.avsample.databinding.ActivityMediaRecorderBinding
import timber.log.Timber
import java.io.File

class MediaRecorderActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMediaRecorderBinding
    private val mVideoRecorderFilePath: String
    private lateinit var mMediaRecorder: MediaRecorder
    private lateinit var mCameraId: String
    private var isRecording = false
    private var videoWidth = 1280
    private var videoHeight = 720

    private val cameraManager: CameraManager by lazy {
        getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    private val characteristics: CameraCharacteristics by lazy {
        cameraManager.getCameraCharacteristics(mCameraId)
    }

    private val cameraThread = HandlerThread("CameraThread").apply { start() }
    private val cameraHandler = Handler(cameraThread.looper)

    private lateinit var cameraSession: CameraCaptureSession
    private lateinit var cameraDevice: CameraDevice

    private val previewRequest: CaptureRequest by lazy {
        cameraSession.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
            addTarget(binding.recorderSurfaceView.holder.surface)
        }.build()
    }

    private val recorderRequest: CaptureRequest by lazy {
        cameraSession.device.createCaptureRequest(CameraDevice.TEMPLATE_RECORD).apply {
            addTarget(binding.recorderSurfaceView.holder.surface)
            addTarget(mMediaRecorder?.surface)
        }.build()
    }

    init {
        val filePath =
            File(
                "${Environment.getExternalStorageDirectory().absolutePath}" + File.separator + "media"
                        + File.separator + "record"
            )
        filePath.mkdirs()
        mVideoRecorderFilePath =
            "$filePath" + File.separator + "media_recorder" + System.currentTimeMillis() + ".mp4"
        Timber.d("##### recorder file path $mVideoRecorderFilePath")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaRecorderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.recorderButton.setOnClickListener(this)

        var cameraIdList = cameraManager.cameraIdList
        mCameraId = cameraIdList[0]
        Timber.d("##### camera id: $mCameraId")

        binding.recorderSurfaceView.holder.addCallback(object : SurfaceHolder.Callback2 {
            override fun surfaceCreated(holder: SurfaceHolder) {
                binding.recorderSurfaceView.setAspectRatio(videoWidth, videoHeight)
                initCamera()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }

            override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
            }
        })

        initMediaRecorder()
        muxer()
    }

    @SuppressLint("MissingPermission")
    private fun initCamera() {
        Timber.d("##### initCamera")
        cameraManager.openCamera(mCameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                Timber.e("##### camera onOpened")
                cameraDevice = camera
                createCaptureSession()
            }

            override fun onDisconnected(camera: CameraDevice) {
                Timber.e("##### camera onDisconnected")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                val msg = when (error) {
                    ERROR_CAMERA_DEVICE -> "Fatal (device)"
                    ERROR_CAMERA_DISABLED -> "Device policy"
                    ERROR_CAMERA_IN_USE -> "Camera in use"
                    ERROR_CAMERA_SERVICE -> "Fatal (service)"
                    ERROR_MAX_CAMERAS_IN_USE -> "Maximum cameras in use"
                    else -> "Unknown"
                }

                Timber.e("##### camera onError $msg")
            }

        }, cameraHandler)
    }

    private fun createCaptureSession() {
        val targets = listOf(binding.recorderSurfaceView.holder.surface)
        cameraDevice.createCaptureSession(
            targets,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cameraSession = session
                    updatePreview()
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                }
            },
            cameraHandler
        )
    }

    private fun updatePreview() {
        cameraSession.setRepeatingRequest(
            previewRequest,
            object : CameraCaptureSession.CaptureCallback() {

            }, cameraHandler
        )
    }

    private fun initMediaRecorder() {
        mMediaRecorder = MediaRecorder().apply {
            setOrientationHint(90)
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)

            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)

            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            //size
            setVideoSize(videoWidth, videoHeight)

            setVideoEncodingBitRate(1_000_000)
            setVideoFrameRate(30)

            setOutputFile(mVideoRecorderFilePath)
        }
    }

    private fun startRecorder() {
        if (isRecording) {
            isRecording = false
            if (null != mMediaRecorder) {
                mMediaRecorder.stop()
                mMediaRecorder.reset()
            }

            binding.root.postDelayed({
                Toast.makeText(
                    this, "Record Finished, File Saved $mVideoRecorderFilePath", Toast.LENGTH_LONG
                ).show()
                finish()
            }, 1000)
            return
        }

        mMediaRecorder.prepare()
        val targets = listOf(binding.recorderSurfaceView.holder.surface, mMediaRecorder.surface)
        cameraSession?.let {
            it.close()
            null
        }
        Timber.d("##### camera session $cameraSession")

        cameraDevice.createCaptureSession(
            targets,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cameraSession = session
                    cameraSession.setRepeatingRequest(recorderRequest, null, cameraHandler)

                    isRecording = true
                    mMediaRecorder.apply {
                        Timber.d("##### start recorder")
                        start()
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                }
            },
            cameraHandler
        )
    }

    private fun muxer() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.recorder_button -> {
                startRecorder()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            cameraDevice.close()
            cameraThread.quitSafely()
        } catch (e: Throwable) {
            Timber.e("##### on destroy", e)
        }
    }
}