package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.QUser;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangjian
 * @date 2018/03/03
 */

@Module
public class QualifierModule {
    @Type("boy")
    @Provides
    public QUser provideBoyUser() {
        return new QUser("boy");
    }

    @Type("girl")
    @Provides
    public QUser provideGirlUser() {
        return new QUser("girl");
    }
}
