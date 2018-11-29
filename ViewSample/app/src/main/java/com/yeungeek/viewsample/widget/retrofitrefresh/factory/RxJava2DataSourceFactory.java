package com.yeungeek.viewsample.widget.retrofitrefresh.factory;

import com.yeungeek.viewsample.widget.retrofitrefresh.Callback;
import com.yeungeek.viewsample.widget.retrofitrefresh.DataSource;
import com.yeungeek.viewsample.widget.retrofitrefresh.DataSourceAdapter;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yangjian
 * @date 2018/10/15
 */

public abstract class RxJava2DataSourceFactory<T> extends DataSourceAdapter.Factory<T> {
    @Override
    public DataSource<T> get() {
        return new RxJava2CallbackDataSource<>(getData());
    }

    public abstract Observable<T> getData();

    static final class RxJava2CallbackDataSource<T> implements DataSource<T> {
        final Observable<T> observable;

        public RxJava2CallbackDataSource(final Observable<T> observable) {
            this.observable = observable;
        }

        @Override
        public void getData(final Callback<T> callback) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<T>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(T t) {
                            callback.onSuccess(t);
                        }

                        @Override
                        public void onError(Throwable e) {
                            callback.onFailure(e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
