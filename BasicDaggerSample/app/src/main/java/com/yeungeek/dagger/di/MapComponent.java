package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.MapActivity;
import com.yeungeek.dagger.vo.SetUser;

import java.util.Map;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/03/08
 */
@Component(modules = MapModule.class)
public interface MapComponent {
    void inject(MapActivity activity);

    Map<String, SetUser> getSetUser();

    Map<Class<?>, SetUser> getSetUseMap();
}
