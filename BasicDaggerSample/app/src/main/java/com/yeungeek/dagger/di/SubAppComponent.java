package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.SubActivity;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/03/08
 */

//@Component(modules = SubModule.class, dependencies = ParentComponent.class)
public interface SubAppComponent {
    void inject(SubActivity activity);
}
