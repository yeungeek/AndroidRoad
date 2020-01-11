package com.yeungeek.dagger.di;

import com.yeungeek.dagger.vo.SetUser;

import dagger.Subcomponent;

/**
 * @author yangjian
 * @date 2018/03/09
 */

@Subcomponent(modules = Sub1Module.class)
public interface Sub1AppComponent {
    SetUser supplySetUser();

    @Subcomponent.Builder
    interface Builder {
        Builder appleModule(Sub1Module module);

        /**
         * @return
         */
        Sub1AppComponent build();
    }
}
