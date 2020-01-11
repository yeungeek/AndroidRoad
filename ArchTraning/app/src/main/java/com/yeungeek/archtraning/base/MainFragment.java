package com.yeungeek.archtraning.base;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yeungeek.archtraning.R;
import com.yeungeek.archtraning.mvc.MvcFragment;
import com.yeungeek.archtraning.mvp.MvpFragment;
import com.yeungeek.archtraning.mvvm.MvvmFragment;
import com.yeungeek.archtraning.paging.PagingFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Main
 *
 * @author
 * @date 2018/09/07
 */

public class MainFragment extends BaseFragment {
    private static final String TAG = "MainFragment";
    private RecyclerView recyclerView;
    private FragmentAdapter adapter;
    private List<FragmentInfo> fragments;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        recyclerView = view.findViewById(R.id.recycle_view);
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new FragmentInfo().setTitle("MVC").setFragment(MvcFragment.class));
        fragments.add(new FragmentInfo().setTitle("MVP").setFragment(MvpFragment.class));
        fragments.add(new FragmentInfo().setTitle("MVVM").setFragment(MvvmFragment.class));
        fragments.add(new FragmentInfo().setTitle("Paging").setFragment(PagingFragment.class));

        adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), R.id.content_fragment, fragments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new LinearDivider(getActivity(), R.color.divider, R.dimen.divider));
        recyclerView.setAdapter(adapter);
    }
}
