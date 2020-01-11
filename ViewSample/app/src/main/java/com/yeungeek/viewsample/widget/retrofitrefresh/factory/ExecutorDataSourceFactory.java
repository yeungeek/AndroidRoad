package com.yeungeek.viewsample.widget.retrofitrefresh.factory;

import android.os.Handler;
import android.os.Looper;

import com.yeungeek.viewsample.widget.retrofitrefresh.Callback;
import com.yeungeek.viewsample.widget.retrofitrefresh.DataSource;
import com.yeungeek.viewsample.widget.retrofitrefresh.DataSourceAdapter;

import java.util.concurrent.Executor;

/**
 * @author yangjian
 * @date 2018/10/15
 */

public abstract class ExecutorDataSourceFactory<T> extends DataSourceAdapter.Factory<T> {
    final Executor callbackExecutor;

    protected ExecutorDataSourceFactory() {
        this.callbackExecutor = new MainThreadExecutor();
    }

    @Override
    public DataSource<T> get() {
        return new ExecutorCallbackDataSource<>(callbackExecutor, getData());
    }

    public abstract T getData();

    static final class ExecutorCallbackDataSource<D> implements DataSource<D> {
        final Executor callbackExecutor;
        final D data;

        ExecutorCallbackDataSource(Executor callbackExecutor, D data) {
            this.callbackExecutor = callbackExecutor;
            this.data = data;
        }

        @Override
        public void getData(final Callback<D> callback) {
            callbackExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(data);
                }
            });
        }
    }

    static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable r) {
            handler.post(r);
        }
    }
}
