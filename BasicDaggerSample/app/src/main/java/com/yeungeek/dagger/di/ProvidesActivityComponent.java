package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.ProvidesActivity;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/02/23
 */
@Component(modules = ProvidesModule.class)
public interface ProvidesActivityComponent {
    void inject(ProvidesActivity activity);
}
