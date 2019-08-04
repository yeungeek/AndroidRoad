package com.yeungeek.mvvm.sample.http;

/**
 * @date 2018/10/19
 */

public interface DataCallback<T> {
    void onResponse(T response);

    void onFailure(Throwable t);
}
// https://proandroiddev.com/concise-error-handling-with-livedata-and-retrofit-15937ceb555b