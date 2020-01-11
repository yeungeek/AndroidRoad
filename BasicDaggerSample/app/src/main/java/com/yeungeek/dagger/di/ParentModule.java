package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.QUser;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangjian
 * @date 2018/03/08
 */

@Module
public class ParentModule {
    @Provides
    QUser provideParent() {
        return new QUser("ParentUser");
    }
}
