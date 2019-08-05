package com.yeungeek.mvvm.core;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.yeungeek.mvvm.utils.VMUtils;

/**
 * @date 2019-08-04
 */

public abstract class BaseVMFragment<VM extends BaseViewModel> extends BaseFragment {
    protected VM viewModel;

    @Override
    public void initViews() {
        initViewModel();
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        initViewObservable();
    }

    private void initViewModel() {
        viewModel = createViewModel(getActivity(), VMUtils.instance(this));
    }

    /**
     * load view model
     */
    public abstract void initViewObservable();

    public <T extends BaseViewModel> T createViewModel(FragmentActivity fragment, Class<T> clazz) {
        return ViewModelProviders.of(fragment).get(clazz);
    }
}
