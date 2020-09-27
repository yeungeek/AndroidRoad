package com.yeungeek.basicjava.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseViewModel<T extends BaseRepo> {
    T repo;

    public BaseViewModel() {
        repo = instance(this);
    }

    private <T> T instance(Object obj) {
        Type type = obj.getClass().getGenericSuperclass();
        if (!ParameterizedType.class.isAssignableFrom(type.getClass())) {
            return null;
        }

        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        try {
            return ((Class<T>) types[0]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    abstract void vmMethod();
}
