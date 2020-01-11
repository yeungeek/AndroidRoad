package com.yeungeek.basictraning.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @date 2018/09/09
 */

public class SDCardUtil {
    public static File getDiskCacheDir(final Context context, final String name) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + name);
    }
}
