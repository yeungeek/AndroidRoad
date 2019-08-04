package com.yeungeek.mvvm.core;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.yeungeek.mvvm.utils.VMUtils;

/**
 * base view model
 *
 * @date 2019-08-04
 */

public abstract class BaseViewModel<T extends BaseRepository> extends AndroidViewModel {
    public T mRepository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mRepository = VMUtils.newInstance(this);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (null != mRepository) {
            mRepository.onDestroy();
        }
    }
}
