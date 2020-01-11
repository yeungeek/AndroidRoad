package com.yeungeek.archtraning.paging;

import android.util.SparseArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yangjian
 * @date 2018/10/09
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }

    public <T extends View> T getView(@IdRes int id) {
        View view = views.get(id);
        if (null == view) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }

        return (T) view;
    }
}
