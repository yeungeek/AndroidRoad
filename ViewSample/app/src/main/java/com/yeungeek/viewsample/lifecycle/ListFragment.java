package com.yeungeek.viewsample.lifecycle;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * @author yangjian
 * @date 2018/11/03
 */

public class ListFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedViewModel viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
    }
}
