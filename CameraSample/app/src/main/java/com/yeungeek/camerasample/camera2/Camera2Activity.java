package com.yeungeek.camerasample.camera2;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yeungeek.camerasample.R;
import com.yeungeek.camerasample.widget.AutoFitTextureView;

/**
 * @date 2019-10-29
 */

public class Camera2Activity extends AppCompatActivity implements View.OnClickListener {
    private AutoFitTextureView mTextureView;
    private ImageButton mTakePictureBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        init();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTextureView.isAvailable()) {
            Log.d("DEBUG", "##### onResume width: " + mTextureView.getWidth() + ", height:" + mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_take_picture:

                break;
        }
    }

    private void init() {

    }

    private void initViews() {
        mTextureView = findViewById(R.id.camera_texture);
        mTakePictureBtn = findViewById(R.id.camera_take_picture);
        mTakePictureBtn.setOnClickListener(this);
    }

    //listener
    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d("DEBUG", "##### onSurfaceTextureAvailable width: " + width + ", height:" + height);

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
}
