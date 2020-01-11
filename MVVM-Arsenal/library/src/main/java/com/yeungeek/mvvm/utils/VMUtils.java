package com.yeungeek.mvvm.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Reflect Utils
 *
 * @date 2019-08-03
 */

public class VMUtils {
    public static <T> Class<T> instance(final Object object) {
        return (Class<T>) getParameterizedTypes(object);
    }

    public static Type getParameterizedTypes(final Object object) {
        Type supperClass = object.getClass().getGenericSuperclass();
        if (!ParameterizedType.class.isAssignableFrom(supperClass.getClass())) {
            return null;
        }

        return ((ParameterizedType) supperClass).getActualTypeArguments()[0];
    }

    public static <T> T newInstance(final Object object) {
        try {
            return (T) instance(object).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
