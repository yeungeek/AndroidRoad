package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.SetUser;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangjian
 * @date 2018/03/09
 */

@Module
public class Sub1Module {

    @Provides
    SetUser provideSetUser() {
        return new SetUser("new sub");
    }
}
