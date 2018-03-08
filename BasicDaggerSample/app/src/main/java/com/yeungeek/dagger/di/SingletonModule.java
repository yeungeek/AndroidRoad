package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.scope.SingletonUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangjian
 * @date 2018/03/08
 */

@Module
public class SingletonModule {
    @Singleton
    @Provides
    SingletonUser provideSingletonUser() {
        return new SingletonUser();
    }
}
