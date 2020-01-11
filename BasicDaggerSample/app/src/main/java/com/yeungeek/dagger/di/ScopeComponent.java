package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.ScopeActivity;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/03/08
 */

@SType
@Component
public interface ScopeComponent {
    void inject(ScopeActivity activity);
}
