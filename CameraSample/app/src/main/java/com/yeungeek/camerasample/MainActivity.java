package com.yeungeek.camerasample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.yeungeek.camerasample.camera1.Camera1Activity;
import com.yeungeek.camerasample.camera2.Camera2Activity;
import com.yeungeek.camerasample.camerasystem.CameraSystemActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mCameraSysBtn;
    private Button mCamera1Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mCameraSysBtn = findViewById(R.id.main_camera_sys_btn);
        mCameraSysBtn.setOnClickListener(this);

        mCamera1Btn = findViewById(R.id.main_camera1_btn);
        mCamera1Btn.setOnClickListener(this);

        findViewById(R.id.main_camera2_photo_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_camera_sys_btn:
                startActivity(new Intent(MainActivity.this, CameraSystemActivity.class));
                break;
            case R.id.main_camera1_btn:
                startActivity(new Intent(MainActivity.this, Camera1Activity.class));
                break;
            case R.id.main_camera2_photo_btn:
                startActivity(new Intent(MainActivity.this, Camera2Activity.class));
                break;
        }
    }
}
