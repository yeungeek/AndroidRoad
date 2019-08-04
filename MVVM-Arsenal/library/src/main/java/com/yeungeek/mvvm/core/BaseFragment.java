package com.yeungeek.mvvm.core;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * base fragment
 *
 * @date 2019-08-04
 */

public abstract class BaseFragment extends Fragment {
    private View rootView;
    //lazy
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(layout(), container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        init(savedInstanceState);
    }

    /**
     * layout
     *
     * @return
     */
    public abstract @LayoutRes
    int layout();

    public abstract void initViews();

    public abstract void init(@Nullable Bundle savedInstanceState);

    protected <T extends View> T getViewById(int id) {
        return (T) rootView.findViewById(id);
    }

}
