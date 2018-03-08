package com.yeungeek.dagger.di;

import com.yeungeek.dagger.ui.NormalSetActivity;
import com.yeungeek.dagger.vo.SetUser;

import java.util.Set;

import dagger.Component;

/**
 * @author yangjian
 * @date 2018/03/08
 */

@Component(modules = {NormalSetModule.class, NormalSetSecondModule.class})
public interface NormalSetComponent {
    void inject(NormalSetActivity activity);

    Set<SetUser> users();
}
