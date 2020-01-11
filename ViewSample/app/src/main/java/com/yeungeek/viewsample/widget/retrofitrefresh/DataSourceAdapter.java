package com.yeungeek.viewsample.widget.retrofitrefresh;

/**
 * @author yangjian
 * @date 2018/10/15
 */

public interface DataSourceAdapter {
    abstract class Factory<T> {
        public abstract DataSource<T> get();

    }
}
