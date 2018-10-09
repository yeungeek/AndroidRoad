package com.yeungeek.archtraning.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yeungeek.archtraning.BR;
import com.yeungeek.archtraning.R;
import com.yeungeek.archtraning.api.Repo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yangjian
 */

public class RepoBindingAdapter extends RecyclerView.Adapter<RepoBindingAdapter.RepoBidingViewHolder> {
    public List<Repo> repos;

    public RepoBindingAdapter() {
        this.repos = new ArrayList<>();
    }

    @NonNull
    @Override
    public RepoBidingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.repo_binding_recycle_item, parent, false);
        RepoBidingViewHolder viewHolder = new RepoBidingViewHolder(binding.getRoot());
        viewHolder.setBinding(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepoBidingViewHolder holder, int position) {
        Repo repo = repos.get(position);
        if (null != repo) {
            holder.getBinding().setVariable(BR.repo, repo);
            holder.getBinding().executePendingBindings();
        }
    }

    public void setData(final List<Repo> repos) {
        if (null == repos) {
            return;
        }
        this.repos.clear();
        this.repos.addAll(repos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return null == repos ? 0 : repos.size();
    }

    static class RepoBidingViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public RepoBidingViewHolder setBinding(ViewDataBinding binding) {
            this.binding = binding;
            return this;
        }

        public RepoBidingViewHolder(@NonNull View itemView) {
            super(itemView);
        }


    }
}
