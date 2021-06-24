package com.yeungeek.camerasample.camera2;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudroom.cloudroomvideosdk.CloudroomVideoMeeting;
import com.cloudroom.cloudroomvideosdk.model.Size;
import com.cloudroom.cloudroomvideosdk.model.VideoCfg;
import com.yeungeek.camerasample.R;

/**
 * @date 2019-10-29
 */

public class Camera2Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mTakePictureBtn;
    private Camera2Manager manager;

    private TextureView mTextureView;
    private TextureView mTextureView1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
//        Camera2Manager2.getInstance().init(this);

        manager = new Camera2Manager(this);
        initViews();
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                manager.setSurfaceTexture(surface);

                mTextureView1.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                    @Override
                    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                        manager.setSurfaceTexture1(surface);
                        manager.startPreview();
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
                });
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
        });

        //manager.startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.stopPreview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_take_picture:
                break;
        }
    }

    private void initViews() {
        mTakePictureBtn = findViewById(R.id.camera_take_picture);
        mTakePictureBtn.setOnClickListener(this);

        mTextureView = findViewById(R.id.texture_view);
        mTextureView1 = findViewById(R.id.texture_view1);
    }

    private void init() {

    }
}
