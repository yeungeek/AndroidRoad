package com.yeungeek.basictraning.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yeungeek.basictraning.R;
import com.yeungeek.basictraning.model.FragmentAdapter;
import com.yeungeek.basictraning.model.FragmentInfo;
import com.yeungeek.basictraning.module.handler.HandlerFragment;
import com.yeungeek.basictraning.module.lru.LruFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @date 2018/09/07
 */

public class MainFragment extends BaseFragment {
    private static final String TAG = "MainFragment";
    private RecyclerView recyclerView;
    private FragmentAdapter adapter;
    private List<FragmentInfo> fragments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.recycle_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }


    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new FragmentInfo().setTitle("LRU Sample").setFragment(LruFragment.class));
        fragments.add(new FragmentInfo().setTitle("Thread Sample").setFragment(LruFragment.class));
        fragments.add(new FragmentInfo().setTitle("Handler Sample").setFragment(HandlerFragment.class));

        adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), R.id.content_fragment, fragments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
