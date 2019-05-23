package com.yeungeek.jnisample;

import android.util.Log;

/**
 * @author jian.yang
 * @date 2019/2/18
 */

public class NativeHelper {
    public NativeHelper() {
    }

    private static String value = "Hello JNI";
    private String localValue = "Local Value";
    private static String nullStr;

    public static native String stringFromJNI();

    public static native void callStaticMethod(int i);

    public static native void callStaticMethod(String str, long i);

    public native void callMethod(int i);

    public native void callMethod(String str, long i);

    public static void staticMethod(String str) {
        Log.d("DEBUG", "##### java: staticMethod: " + str);
        Log.d("DEBUG", "##### value change: " + value);
        Log.d("DEBUG", "##### null str: " + nullStr.toLowerCase());
    }

    public void normalMethod(String str) {
        Log.d("DEBUG", "##### java: method: " + str);
        Log.d("DEBUG", "##### set value: " + localValue);
    }
}
