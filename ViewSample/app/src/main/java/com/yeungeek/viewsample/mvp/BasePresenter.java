package com.yeungeek.viewsample.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.util.Log;

/**
 * @date 2018/08/17
 */

public class BasePresenter implements IPresenter {
    @Override
    public void onCreate(LifecycleOwner owner) {
        Log.d("DEBUG", "-----> BasePresenter onCreate");
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        Log.d("DEBUG", "-----> BasePresenter onStart");
    }

    @Override
    public void onStop(LifecycleOwner owner) {
        Log.d("DEBUG", "-----> BasePresenter onStop");
    }

    @Override
    public void onPause(LifecycleOwner owner) {
        Log.d("DEBUG", "-----> BasePresenter onPause");
    }

    @Override
    public void onResume(LifecycleOwner owner) {
        Log.d("DEBUG", "-----> BasePresenter onResume");
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        Log.d("DEBUG", "-----> BasePresenter onDestroy");

    }

    @Override
    public void onLifecycleChanged(LifecycleOwner owner, Lifecycle.Event event) {
        Log.d("DEBUG", "-----> BasePresenter onLifecycleChanged " + event.name());

    }
}
