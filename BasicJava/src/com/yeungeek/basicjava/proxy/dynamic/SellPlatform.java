package com.yeungeek.basicjava.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SellPlatform implements InvocationHandler {
    private Object object;

    public SellPlatform(final Object obj) {
        this.object = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("start sell: " + this.getClass().getSimpleName());
        method.invoke(object, args);
        System.out.println("end sell");
        return null;
    }
}
