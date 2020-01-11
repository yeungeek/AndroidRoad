package com.yeungeek.dagger.android;

import com.yeungeek.dagger.App;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * @author yangjian
 * @date 2018/03/09
 */

@Component(modules = AndroidInjectionModule.class)
public interface AppComponent {
    void inject(App app);
}
