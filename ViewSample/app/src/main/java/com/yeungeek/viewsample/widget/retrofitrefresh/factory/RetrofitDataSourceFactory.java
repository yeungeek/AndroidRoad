package com.yeungeek.viewsample.widget.retrofitrefresh.factory;

import com.yeungeek.viewsample.widget.retrofitrefresh.Callback;
import com.yeungeek.viewsample.widget.retrofitrefresh.DataSource;
import com.yeungeek.viewsample.widget.retrofitrefresh.DataSourceAdapter;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author yangjian
 * @date 2018/10/15
 */

public abstract class RetrofitDataSourceFactory<T> extends DataSourceAdapter.Factory<T> {
    @Override
    public DataSource<T> get() {
        return new RetrofitCallbackDataSource<>(getData());
    }

    public abstract Call<T> getData();

    static final class RetrofitCallbackDataSource<T> implements DataSource<T> {
        final Call<T> call;

        public RetrofitCallbackDataSource(final Call<T> call) {
            this.call = call;
        }

        @Override
        public void getData(final Callback<T> callback) {
            call.enqueue(new retrofit2.Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    callback.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    callback.onFailure(t);
                }
            });
        }
    }
}
