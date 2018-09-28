package com.yeungeek.modulartraning.fragment;

import android.support.v4.app.Fragment;

/**
 * @date 2018/09/07
 */

public class FragmentInfo {
    private String title;
    private Fragment fragment;

    public String getTitle() {
        return title;
    }

    public FragmentInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public FragmentInfo setFragment(Fragment fragment) {
        this.fragment = fragment;
        return this;
    }
}
