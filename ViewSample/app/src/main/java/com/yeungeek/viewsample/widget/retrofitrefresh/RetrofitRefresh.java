package com.yeungeek.viewsample.widget.retrofitrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import java.util.List;

/**
 * @author yangjian
 */

public class RetrofitRefresh extends FrameLayout {
    private RecyclerView recyclerView;
    private LayoutAdapter.Factory adapterFactory;
    private DataSourceAdapter.Factory dataSourceFactory;
    private RefreshAdapter adapter;

    public RetrofitRefresh(@NonNull Context context, LayoutAdapter.Factory adapterFactory, DataSourceAdapter.Factory dataSourceFactory) {
        super(context);
        this.adapterFactory = adapterFactory;
        this.dataSourceFactory = dataSourceFactory;
        init();
    }

    private void init() {
        //refresh
        adapter = adapterFactory.getAdapter();
        this.recyclerView = new RecyclerView(getContext());
        this.recyclerView.setLayoutManager(null == adapterFactory.getLayoutManager() ?
                new LinearLayoutManager(getContext()) : adapterFactory.getLayoutManager());
        this.recyclerView.setAdapter(adapter);
        addView(this.recyclerView, new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    public LayoutAdapter.Factory getAdapterFactory() {
        return adapterFactory;
    }

    public void load() {
        //1. data source
        //2. callback
        dataSourceFactory.get().getData(new Callback() {
            @Override
            public void onSuccess(Object data) {
                adapter.updateList((List) data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    public static final class Builder {
        private Context context;
        private LayoutAdapter.Factory layoutFactory;
        private DataSourceAdapter.Factory dataSourceFactory;

        public Builder(final Context context) {
            this.context = context;
        }

        public Builder setLayoutFactory(LayoutAdapter.Factory layoutFactory) {
            this.layoutFactory = layoutFactory;
            return this;
        }

        public Builder setDataSourceFactory(DataSourceAdapter.Factory dataSourceFactory) {
            this.dataSourceFactory = dataSourceFactory;
            return this;
        }

        public RetrofitRefresh build() {
            return new RetrofitRefresh(context, layoutFactory, dataSourceFactory);
        }
    }
}
