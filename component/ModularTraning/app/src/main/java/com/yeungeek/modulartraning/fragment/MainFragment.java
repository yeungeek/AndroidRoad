package com.yeungeek.modulartraning.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yeungeek.library.BaseFragment;
import com.yeungeek.library.router.Consts;
import com.yeungeek.library.router.RouterUtils;
import com.yeungeek.library.ui.LinearDivider;
import com.yeungeek.modulartraning.R;

import java.util.ArrayList;
import java.util.List;


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
        return R.layout.app_fragment_main;
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new FragmentInfo().setTitle("MVP").setFragment(RouterUtils.getFragment(Consts.MODULEA_MVP_FRAGMENT)));

        adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), R.id.content_fragment, fragments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new LinearDivider(getActivity(), R.color.divider, R.dimen.divider));
        recyclerView.setAdapter(adapter);
    }
}
