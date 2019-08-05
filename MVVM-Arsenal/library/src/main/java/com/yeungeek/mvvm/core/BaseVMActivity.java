package com.yeungeek.mvvm.core;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yeungeek.mvvm.utils.VMUtils;

/**
 * @date 2019-08-04
 */

public abstract class BaseVMActivity<VM extends BaseViewModel> extends BaseActivity {
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
        viewModel = createViewModel(this, VMUtils.instance(this));
    }

    private <VM extends BaseViewModel> VM createViewModel(AppCompatActivity activity, Class<VM> clazz) {
        if (null == clazz) {
            return null;
        }
        return ViewModelProviders.of(activity).get(clazz);
    }

    protected abstract void initViewObservable();
}
