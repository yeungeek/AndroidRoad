package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.SingletonActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/03/08
 */

@Singleton
@Component(modules = SingletonModule.class)
public interface SingletonComponent {
    void inject(SingletonActivity activity);
}
