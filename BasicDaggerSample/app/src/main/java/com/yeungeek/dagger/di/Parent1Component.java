package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.SubActivity;
import com.yeungeek.dagger.vo.QUser;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/03/08
 */

@Component(modules = Parent1Module.class)
public interface Parent1Component {
    void inject(SubActivity activity);

    Sub1AppComponent.Builder supplySubComponentBuilder();
}
