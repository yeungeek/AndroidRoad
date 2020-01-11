package com.yeungeek.dagger.android;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * @author yangjian
 * @date 2018/03/09
 */

@Subcomponent(modules = AndroidInjectionModule.class)
public interface MainSubComponent extends AndroidInjector<AppActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<AppActivity> {
    }
}
