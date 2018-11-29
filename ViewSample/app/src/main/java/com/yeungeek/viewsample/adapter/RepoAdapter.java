package com.yeungeek.viewsample.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yeungeek.viewsample.R;
import com.yeungeek.viewsample.api.Repo;
import com.yeungeek.viewsample.widget.retrofitrefresh.RefreshAdapter;

/**
 * @author yangjian
 * @date 2018/10/15
 */

public class RepoAdapter extends RefreshAdapter<Repo, RepoAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepoAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_recycle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Repo repo = getItem(position);
        if (null != repo) {
            holder.text.setText(repo.getName());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
