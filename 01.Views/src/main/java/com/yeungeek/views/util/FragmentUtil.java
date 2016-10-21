package com.yeungeek.views.util;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;

public class FragmentUtil {
    private final HashMap<String, Fragment> fragments = new HashMap<>();

    private final FragmentActivity activity;
    private final FragmentManager manager;
    private final int container;

    private String current;

    public FragmentUtil(FragmentActivity activity, @IdRes int container) {
        this.activity = activity;
        this.container = container;
        this.manager = activity.getSupportFragmentManager();
    }

    public void switchTo(Class<? extends Fragment> fragment) {
        String name = fragment.getName();
        if (null != current) {
            manager.beginTransaction().hide(fragments.get(current)).commit();
        }

        if (null == manager.findFragmentByTag(name)) {
            Fragment instance = Fragment.instantiate(activity, name);
            fragments.put(name, instance);
            manager.beginTransaction().add(container, instance, name).addToBackStack(name).commit();
        } else {
            manager.beginTransaction().show(fragments.get(name)).commit();
        }

        current = name;
    }

    @Nullable
    public Fragment getFragment(Class<? extends Fragment> fragment) {
        return fragments.get(fragment.getName());
    }
}
