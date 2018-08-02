package com.yeungeek.hencoder.draw1.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.WindowManager;

/**
 * @author yangjian
 * @date 2018/08/02
 */

public class AndroidUtils {
    public static Pair<Integer, Integer> getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return new Pair<>(metrics.widthPixels, metrics.heightPixels);
    }
}
