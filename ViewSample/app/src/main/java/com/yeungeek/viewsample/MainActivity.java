package com.yeungeek.viewsample;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.yeungeek.viewsample.mvp.PresenterActivity;
import com.yeungeek.viewsample.widget.LabelsView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LabelsView mLabels;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLabels = findViewById(R.id.labels);

        List<String> arr = new ArrayList<>();
        arr.add("1.测试第一条");
        arr.add("2.测试第二条");
        arr.add("3.测试第三条");
        arr.add("4.测试");
        arr.add("5.我是测试第四条");
        arr.add("6.测试第四条");

        mLabels.addLabels(arr);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PresenterActivity.class));
            }
        });
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        Log.d("MainActivity", "onContentChanged");
    }

    private void addWindow() {
        Button floatingButton = new Button(this);
        floatingButton.setText("button");
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                PixelFormat.TRANSPARENT
        );

        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.gravity = Gravity.CENTER;
        WindowManager windowManager = getWindowManager();
        windowManager.addView(floatingButton, layoutParams);
    }
}
