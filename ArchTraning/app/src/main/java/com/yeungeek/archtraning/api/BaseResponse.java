package com.yeungeek.archtraning.api;

/**
 * @author yangjian
 * @date 2018/10/09
 */

public class BaseResponse<T> {
    private String error;
    private T results;

    public String getError() {
        return error;
    }

    public BaseResponse<T> setError(String error) {
        this.error = error;
        return this;
    }

    public T getResults() {
        return results;
    }

    public BaseResponse<T> setResults(T results) {
        this.results = results;
        return this;
    }
}
