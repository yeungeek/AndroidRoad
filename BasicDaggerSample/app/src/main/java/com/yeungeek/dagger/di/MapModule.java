package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.MapActivity;
import com.yeungeek.dagger.vo.SetUser;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

/**
 * @author yangjian
 * @date 2018/03/08
 */

@Module
public class MapModule {
    @Provides
    @IntoMap
    @StringKey("first")
    SetUser providerPUser() {
        return new SetUser("map1");
    }

    @Provides
    @IntoMap
    @ClassKey(MapActivity.class)
    SetUser providerPUserMap() {
        return new SetUser("map2");
    }
}
