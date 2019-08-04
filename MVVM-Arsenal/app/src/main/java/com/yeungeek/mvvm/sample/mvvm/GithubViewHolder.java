package com.yeungeek.mvvm.sample.mvvm;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.yeungeek.mvvm.core.BaseViewModel;
import com.yeungeek.mvvm.sample.http.DataCallback;
import com.yeungeek.mvvm.sample.data.Repo;

import java.util.List;

/**
 * @date 2018/10/19
 */

public class GithubViewHolder extends BaseViewModel<GithubRepository> {
    private MutableLiveData<List<Repo>> repoData;

    public GithubViewHolder(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Repo>> repoData() {
        if (null == repoData) {
            repoData = new MutableLiveData<>();
        }
        return repoData;
    }

    public void getRepoList(final String name) {
        mRepository.getRepoList(name, new DataCallback<List<Repo>>() {
            @Override
            public void onResponse(List<Repo> response) {
                repoData.setValue(response);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
