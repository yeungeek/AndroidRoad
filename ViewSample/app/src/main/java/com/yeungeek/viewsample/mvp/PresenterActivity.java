package com.yeungeek.viewsample.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yeungeek.viewsample.R;

/**
 * @author yangjian
 * @date 2018/08/17
 */

public class PresenterActivity extends AppCompatActivity {
    private IPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new BasePresenter();
        getLifecycle().addObserver(mPresenter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG","-----> PresenterActivity onDestroy");
    }
}
