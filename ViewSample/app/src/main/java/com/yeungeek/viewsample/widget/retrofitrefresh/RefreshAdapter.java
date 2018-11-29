package com.yeungeek.viewsample.widget.retrofitrefresh;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2018/10/08
 */

public abstract class RefreshAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> data = new ArrayList<>();

    public void updateList(final List<T> newData) {
        if (null == newData || newData.isEmpty()) {
            return;
        }

        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public void appendList(final List<T> newData) {
        if (null == newData || newData.isEmpty()) {
            return;
        }

        int position = data.size() == 0 ? 0 : data.size() - 1;
        data.addAll(newData);
        notifyItemRangeChanged(position, data.size() - position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public T getItem(int position) {
        return data.get(position);
    }

    public List<T> getData() {
        return data;
    }
}
