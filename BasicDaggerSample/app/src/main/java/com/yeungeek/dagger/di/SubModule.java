package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.SetUser;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangjian
 * @date 2018/03/08
 */

//@Module
public class SubModule {
//    @Provides
    SetUser providerSubUser() {
        return new SetUser("SubUser");
    }
}
