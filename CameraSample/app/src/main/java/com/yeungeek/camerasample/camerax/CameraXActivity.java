package com.yeungeek.camerasample.camerax;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.content.ContextCompat;

import com.yeungeek.camerasample.R;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class CameraXActivity extends AppCompatActivity {
    private ImageButton mTakePicture;
    private TextureView mTextureView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camerax);

        mTakePicture = findViewById(R.id.camerax_take_picture);

        mTextureView = findViewById(R.id.camerax_texture);

        startCamera();
    }

    @SuppressLint("RestrictedApi")
    private void startCamera() {
        //1. preview
        PreviewConfig config = new PreviewConfig.Builder()
                .setLensFacing(CameraX.LensFacing.BACK)
//                .setTargetRotation(mTextureView.getDisplay().getRotation())
                .setTargetResolution(new Size(640, 480))
                .build();

        Preview preview = new Preview(config);
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(@NonNull Preview.PreviewOutput output) {
                Log.d("DEBUG","##### " + output);
                if (mTextureView.getParent() instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) mTextureView.getParent();
                    viewGroup.removeView(mTextureView);
                    viewGroup.addView(mTextureView, 0);

                    mTextureView.setSurfaceTexture(output.getSurfaceTexture());
                    updateTransform();
                }
            }
        });

        //2. capture
        ImageCaptureConfig captureConfig = new ImageCaptureConfig.Builder()
//                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();

        ImageCapture imageCapture = new ImageCapture(captureConfig);
        mTakePicture.setOnClickListener((view) -> {
            final File file = new File(getExternalMediaDirs()[0], System.currentTimeMillis() + ".jpg");
            Log.d("DEBUG", "##### file path: " + file.getPath());
            imageCapture.takePicture(file, ContextCompat.getMainExecutor(getApplicationContext()), new ImageCapture.OnImageSavedListener() {
                @Override
                public void onImageSaved(@NonNull File file) {
                    Log.d("DEBUG", "##### onImageSaved: " + file.getPath());
                }

                @Override
                public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
                    Log.d("DEBUG", "##### onError: " + message);
                }
            });
        });

        //3. image analysis
        ImageAnalysisConfig analysisConfig = new ImageAnalysisConfig.Builder()
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis(analysisConfig);
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(getApplicationContext()),
                new LuminosityAnalyzer());

        CameraX.bindToLifecycle(this, /*preview,*/imageCapture, imageAnalysis);
    }

    private void updateTransform() {
        int centerX = mTextureView.getWidth() / 2;
        int centerY = mTextureView.getHeight() / 2;

        int rotationDegrees = 0;

        switch (mTextureView.getDisplay().getRotation()) {
            case Surface.ROTATION_0:
                rotationDegrees = 0;
                break;
            case Surface.ROTATION_90:
                rotationDegrees = 90;
                break;
            case Surface.ROTATION_180:
                rotationDegrees = 180;
                break;
            case Surface.ROTATION_270:
                rotationDegrees = 270;
                break;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(-rotationDegrees, centerX, centerY);
        mTextureView.setTransform(matrix);
    }

    private class LuminosityAnalyzer implements ImageAnalysis.Analyzer {
        private long lastAnalyzedTimestamp = 0L;

        @Override
        public void analyze(ImageProxy image, int rotationDegrees) {
            final Image img = image.getImage();
            if (img != null) {
                Log.d("DEBUG", img.getWidth() + "," + img.getHeight());
            }
        }
    }
}
