package com.yeungeek.basictraning.module.lru;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yeungeek.basictraning.R;
import com.yeungeek.basictraning.fragments.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @date 2018/09/09
 */

public class LruFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_lru, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
