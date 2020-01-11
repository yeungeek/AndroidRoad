package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.PUser;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangjian
 * @date 2018/02/23
 */

@Module
public class ProvidesModule {
    public ProvidesModule() {
    }

    @Provides
    public PUser provideUser() {
        return new PUser();
    }
}
