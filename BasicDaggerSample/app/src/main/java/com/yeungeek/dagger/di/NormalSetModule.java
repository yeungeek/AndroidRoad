package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.SetUser;

import java.util.HashSet;
import java.util.Set;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import dagger.multibindings.IntoSet;

/**
 * @author yangjian
 * @date 2018/03/08
 */

@Module
public class NormalSetModule {

    @Provides
    @IntoSet
    SetUser provideSetUser() {
        return new SetUser("1");
    }

    @Provides
    @ElementsIntoSet
    Set<SetUser> provideSetUsers() {
        Set<SetUser> users = new HashSet<>();
        users.add(new SetUser("3"));
        users.add(new SetUser("4"));
        return users;
    }
}
