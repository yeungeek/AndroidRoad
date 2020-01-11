package com.yeungeek.archtraning.base;

/**
 * @date 2018/09/07
 */

public class FragmentInfo {
    private String title;
    private Class<?> fragment;

    public String getTitle() {
        return title;
    }

    public FragmentInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public Class<?> getFragment() {
        return fragment;
    }

    public FragmentInfo setFragment(Class<?> fragment) {
        this.fragment = fragment;
        return this;
    }
}
