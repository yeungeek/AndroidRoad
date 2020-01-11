package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.QualifierActivity;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/03/03
 */
@Component(modules = QualifierModule.class)
public interface QualifierActivityComponent {
    void inject(QualifierActivity activity);
}
