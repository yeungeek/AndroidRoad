package com.yeungeek.viewsample.widget.retrofitrefresh;

/**
 * @author yangjian
 * @date 2018/10/15
 */

public interface DataSourceAdapter<T> {

    T adapt();

    abstract class Factory {

    }
}
