package com.yeungeek.archtraning.paging;

import android.os.Bundle;
import android.view.View;

import com.yeungeek.archtraning.R;
import com.yeungeek.archtraning.api.GankData;
import com.yeungeek.archtraning.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yangjian
 * @date 2018/10/09
 */

public class PagingFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private GankViewModel gankViewModel;
    private GankAdapter gankAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_paging;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycle_view);
        gankViewModel = ViewModelProviders.of(this).get(GankViewModel.class);

        gankAdapter = new GankAdapter(new DiffUtil.ItemCallback<GankData>() {
            @Override
            public boolean areItemsTheSame(@NonNull GankData oldItem, @NonNull GankData newItem) {
                return oldItem.get_id() == newItem.get_id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull GankData oldItem, @NonNull GankData newItem) {
                return oldItem.getUrl().equals(newItem.getUrl());
            }
        });

        recyclerView.setAdapter(gankAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gankViewModel.getData().observe(this, new Observer<PagedList<GankData>>() {
            @Override
            public void onChanged(PagedList<GankData> gankData) {
                gankAdapter.submitList(gankData);
            }
        });
    }
}
