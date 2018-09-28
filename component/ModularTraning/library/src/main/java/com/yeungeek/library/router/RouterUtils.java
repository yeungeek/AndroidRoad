package com.yeungeek.library.router;

import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author yangjian
 * @date 2018/09/28
 */

public class RouterUtils {
    public static void routerInject() {

    }

    public static Object navigation(final String path) {
        return ARouter.getInstance().build(path).navigation();
    }

    public static Fragment getFragment(final String path) {
        return (Fragment) navigation(path);
    }
}
