package com.yeungeek.mvvm.sample.ui;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yeungeek.mvvm.core.BaseVMActivity;
import com.yeungeek.mvvm.sample.R;
import com.yeungeek.mvvm.sample.data.Repo;
import com.yeungeek.mvvm.sample.mvvm.GithubViewHolder;

import java.util.List;

/**
 * @date 2019-08-04
 */

public class GithubActivity extends BaseVMActivity<GithubViewHolder> {
    private RecyclerView mRecyclerView;
    private GithubAdapter mAdapter;

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.recycle_view);
    }

    @Override
    protected void initViewObservable() {
        viewModel.repoData().observe(this, new Observer<List<Repo>>() {
            @Override
            public void onChanged(@Nullable List<Repo> repos) {
                if (null != repos) {
                    mAdapter.setData(repos);
                }
            }
        });
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mAdapter = new GithubAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);
        viewModel.getRepoList("yeungeek");
    }


    @Override
    public int layout() {
        return R.layout.github_recycle_view;
    }
}
