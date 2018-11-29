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
import android.widget.FrameLayout;

import com.yeungeek.viewsample.adapter.RepoAdapter;
import com.yeungeek.viewsample.api.GithubApi;
import com.yeungeek.viewsample.api.Repo;
import com.yeungeek.viewsample.api.ServiceGenerator;
import com.yeungeek.viewsample.mvp.PresenterActivity;
import com.yeungeek.viewsample.widget.LabelsView;
import com.yeungeek.viewsample.widget.retrofitrefresh.LayoutAdapter;
import com.yeungeek.viewsample.widget.retrofitrefresh.RefreshAdapter;
import com.yeungeek.viewsample.widget.retrofitrefresh.RetrofitRefresh;
import com.yeungeek.viewsample.widget.retrofitrefresh.factory.RxJava2DataSourceFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity {
    private LabelsView mLabels;
    private FrameLayout mRecycleList;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLabels = findViewById(R.id.labels);

        final List<String> arr = new ArrayList<>();
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

        mRecycleList = findViewById(R.id.recycle);

        final GithubApi api = ServiceGenerator.createService(GithubApi.class);

        final RetrofitRefresh recycleView = new RetrofitRefresh.Builder(getApplicationContext())
                .setLayoutFactory(new LayoutAdapter.Factory() {
                    @Override
                    public RefreshAdapter getAdapter() {
                        return new RepoAdapter();
                    }
                }).setDataSourceFactory(new RxJava2DataSourceFactory<List<Repo>>() {
                    @Override
                    public Observable<List<Repo>> getData() {
                        return api.obsRespo("yeungeek");
                    }
                }).build();


        mRecycleList.addView(recycleView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        recycleView.load();
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
