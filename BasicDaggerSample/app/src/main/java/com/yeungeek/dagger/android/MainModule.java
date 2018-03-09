package com.yeungeek.dagger.android;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * @author yangjian
 * @date 2018/03/09
 */

@Module(subcomponents = MainSubComponent.class)
public abstract class MainModule {
    @Binds
    @IntoMap
    @ActivityKey(AppActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindActivityFactory(MainSubComponent.Builder builder);
}
