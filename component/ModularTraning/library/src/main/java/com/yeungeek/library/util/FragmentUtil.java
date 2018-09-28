package com.yeungeek.library.util;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Fragment util
 *
 * @author yangjian
 * @date 2018/09/07
 */

public class FragmentUtil {
    public static void pushFragmentToBackStack(FragmentManager manager, Fragment fragment, final int containerId) {
        pushFragmentToBackStack(manager, fragment, fragment.getClass().toString(), containerId);
    }

    public static void pushFragmentToBackStack(FragmentManager manager, Fragment fragment, final String fragmentTag, final int containerId) {
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(containerId, fragment, fragmentTag);
        }

        ft.addToBackStack(fragmentTag);
        ft.commitAllowingStateLoss();
    }

    public static void pushFragmentToBackStack(FragmentManager manager, Class<?> clazz, final int containerId) {
        if (null == clazz) {
            return;
        }

        String fragmentTag = clazz.toString();
        Fragment fragment = manager.findFragmentByTag(fragmentTag);
        if (null == fragment) {
            try {
                fragment = (Fragment) clazz.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        //add
        pushFragmentToBackStack(manager, fragment, fragmentTag, containerId);
    }
}
