package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.InjectActivity;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/02/23
 */

@Component
public interface InjectActivityComponent {
    void inject(InjectActivity activity);
}
