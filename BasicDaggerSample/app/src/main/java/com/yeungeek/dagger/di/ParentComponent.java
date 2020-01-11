package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.QUser;
import com.yeungeek.dagger.vo.SetUser;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/03/08
 */

@Component(modules = ParentModule.class)
public interface ParentComponent {
    QUser supplyQUser();
}
