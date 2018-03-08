package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.LazyActivity;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/03/08
 */

//@SType
@Component
public interface LazyComponent {
    void inject(LazyActivity activity);
}
