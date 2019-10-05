package com.yeungeek.camerasample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yeungeek.camerasample.camerasystem.CameraSystemActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mCameraSysBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mCameraSysBtn = findViewById(R.id.main_camera_sys_btn);
        mCameraSysBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_camera_sys_btn:
                startActivity(new Intent(MainActivity.this, CameraSystemActivity.class));
                break;
        }
    }
}
