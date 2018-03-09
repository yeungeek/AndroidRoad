package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.PUser;
import com.yeungeek.dagger.vo.QUser;

import dagger.Module;
import dagger.Provides;

/**
 * @author yangjian
 * @date 2018/03/09
 */

@Module(subcomponents = Sub1AppComponent.class)
public class Parent1Module {
    @Provides
    QUser provideQUser() {
        return new QUser("new parent");
    }

    @Provides
    @Type("sub")
    QUser provideSubQuser(Sub1AppComponent.Builder builder) {
        return new QUser(builder.appleModule(new Sub1Module()).build().supplySetUser().toString());
    }
}