package com.yeungeek.mvvm.core;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * BaseActivity
 *
 * @date 2019-08-03
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());
        initViews();
        init(savedInstanceState);
    }

    public abstract @LayoutRes
    int layout();

    public abstract void initViews();

    public abstract void init(@Nullable Bundle savedInstanceState);
}
