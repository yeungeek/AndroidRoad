package com.yeungeek.basictraning.fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @date 2018/09/07
 */

public class FragmentUtil {
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
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(containerId, fragment, fragmentTag);
        }

        ft.addToBackStack(fragmentTag);
        ft.commitAllowingStateLoss();
    }
}
