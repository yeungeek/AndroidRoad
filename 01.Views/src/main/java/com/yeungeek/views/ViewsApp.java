package com.yeungeek.views;


import android.app.Application;

import hugo.weaving.DebugLog;
import timber.log.Timber;

public class ViewsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initComponent();

        Timber.d("----> application create");
    }

    @DebugLog
    private void initComponent() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
