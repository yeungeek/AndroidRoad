package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.SetUser;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

/**
 * @author yangjian
 * @date 2018/03/08
 */

@Module
public class NormalSetSecondModule {

    @Provides
    @IntoSet
    SetUser provideSetUser() {
        return new SetUser("2");
    }
}
