package com.yeungeek.viewsample.widget.retrofitrefresh;

import android.support.v7.widget.RecyclerView;

/**
 * @author yangjian
 * @date 2018/10/08
 */

public interface LayoutAdapter {
    abstract class Factory {
        public RecyclerView.LayoutManager getLayoutManager() {
            return null;
        }

        public abstract RefreshAdapter getAdapter();
    }
}
