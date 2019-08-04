package com.yeungeek.mvvm.sample.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yeungeek.mvvm.sample.R;
import com.yeungeek.mvvm.sample.data.Repo;

import java.util.ArrayList;
import java.util.List;


/**
 * @date 2018/10/08
 */

public class GithubAdapter extends RecyclerView.Adapter<GithubAdapter.ViewHolder> {
    private List<Repo> repoList = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_recycle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(repoList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }

    public void setData(final List<Repo> repos) {
        this.repoList.clear();
        this.repoList.addAll(repos);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
