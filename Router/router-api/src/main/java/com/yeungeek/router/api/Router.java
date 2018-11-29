package com.yeungeek.router.api;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import com.yeungeek.router.annotation.RouterUri;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author yangjian
 * @date 2018/11/16
 */

public class Router {
    private Context context;

    public Router(Builder builder) {
        context = builder.context;
    }

    public <T> T create(final Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RouterUri routerUri = method.getAnnotation(RouterUri.class);
                Log.d("DEBUG", "####### " + args + ",value: " + routerUri.value());

                performStart(routerUri.value());
                return null;
            }
        });
    }

    public static final class Builder {
        private Context context;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Router build() {
            return new Router(this);
        }
    }

    private void performStart(String routerUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(routerUri));
        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> infoList = manager.queryIntentActivities(intent, 0);
        if (!infoList.isEmpty()) {
            context.startActivity(intent);
        }
    }
}
