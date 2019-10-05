package com.yeungeek.camerasample.camerasystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yeungeek.camerasample.R;

/**
 * @date 2019-10-02
 */

public class CameraSystemActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mTakePhotoBtn;
    private ImageView mDisplayImageIv;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_system);
        initViews();
    }

    private void initViews() {
        mTakePhotoBtn = findViewById(R.id.take_photo_btn);
        mTakePhotoBtn.setOnClickListener(this);

        mDisplayImageIv = findViewById(R.id.display_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo_btn:
                takePhoto();
                break;
        }
    }

    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("DEBUG", "##### onActivityResult: " + requestCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");

            mDisplayImageIv.setImageBitmap(bitmap);
        }
    }
}
