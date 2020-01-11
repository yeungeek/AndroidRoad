package com.yeungeek.mvvm.sample;

import android.app.Application;

import timber.log.Timber;

/**
 * @date 2019-08-04
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initLogs();
    }

    private void initLogs() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
