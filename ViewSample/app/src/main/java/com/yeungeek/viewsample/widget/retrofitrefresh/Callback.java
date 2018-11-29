package com.yeungeek.viewsample.widget.retrofitrefresh;

/**
 * @author yangjian
 * @date 2018/10/15
 */

public interface Callback<T> {
    void onSuccess(T data);

    void onFailure(Throwable e);
}
