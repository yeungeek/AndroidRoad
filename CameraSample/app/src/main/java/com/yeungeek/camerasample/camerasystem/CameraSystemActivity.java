package com.yeungeek.camerasample.camerasystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.yeungeek.camerasample.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @date 2019-10-02
 */

public class CameraSystemActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mTakePhotoBtn;
    private Button mTakeFullPhotoBtn;

    private ImageView mDisplayImageIv;
    private String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_system);
        initViews();
    }

    private void initViews() {
        mTakePhotoBtn = findViewById(R.id.take_photo_btn);
        mTakeFullPhotoBtn = findViewById(R.id.take_full_photo_btn);
        mTakePhotoBtn.setOnClickListener(this);
        mTakeFullPhotoBtn.setOnClickListener(this);

        mDisplayImageIv = findViewById(R.id.display_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo_btn:
                takePhoto();
                break;
            case R.id.take_full_photo_btn:
                takeFullPhoto();
                break;
        }
    }

    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void takeFullPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (null != photoFile) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.yeungeek.camerasample.fileprovider", photoFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = new File(Environment.getExternalStorageDirectory(), "Camera");
        Log.d("DEBUG", "##### storageDir: " + storageDir.getAbsolutePath());
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("DEBUG", "##### mCurrentPhotoPath: " + mCurrentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("DEBUG", "##### onActivityResult: " + requestCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");

            mDisplayImageIv.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            broadcastScan();
            connectionScan();
            setScaleBitmap(mDisplayImageIv);
        }
    }

    private void broadcastScan() {
        /**
         * MediaScannerReceiver
         *
         * must be start Environment.getExternalStorageDirectory()
         */
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(mCurrentPhotoPath)));
        this.sendBroadcast(intent);
    }

    private void connectionScan() {
        MediaScannerConnection.scanFile(this, new String[]{new File(mCurrentPhotoPath).getParent()},
                new String[]{"image/jpeg"}, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("DEBUG", "##### onScanCompleted: " + path);
                    }
                });
    }


    private void setScaleBitmap(final ImageView imageView) {
        int imgW = imageView.getWidth();
        int imgH = imageView.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mCurrentPhotoPath, options);

        int bitmapW = options.outWidth;
        int bitmapH = options.outHeight;

        int scaleFactor = Math.min(bitmapW / imgW, bitmapH / imgH);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
        imageView.setImageBitmap(bitmap);
    }
}
