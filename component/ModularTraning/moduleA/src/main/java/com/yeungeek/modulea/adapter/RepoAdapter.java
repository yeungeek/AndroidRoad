package com.yeungeek.modulea.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yeungeek.library.data.model.Repo;
import com.yeungeek.modulea.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yangjian
 * @date 2018/09/20
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {
    private List<Repo> mRepos = new ArrayList<>();

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_recycle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repo repo = mRepos.get(position);
        if (null != repo) {
            holder.mRepoName.setText(repo.name);
        }
    }

    @Override
    public int getItemCount() {
        return null == mRepos ? 0 : mRepos.size();
    }

    public void setData(final List<Repo> repos) {
        if (null == repos) {
            return;
        }
        this.mRepos.clear();
        this.mRepos.addAll(repos);
        notifyDataSetChanged();
    }

    static class RepoViewHolder extends RecyclerView.ViewHolder {
        TextView mRepoName;

        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
            mRepoName = itemView.findViewById(R.id.text);
        }
    }
}
