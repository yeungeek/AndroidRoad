package com.yeungeek.mvvm.sample.mvvm;


import com.yeungeek.mvvm.core.BaseRepository;
import com.yeungeek.mvvm.sample.http.DataCallback;
import com.yeungeek.mvvm.sample.http.GithubApi;
import com.yeungeek.mvvm.sample.data.Repo;
import com.yeungeek.mvvm.sample.http.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * @author yangjian
 * @date 2018/10/19
 */

public class GithubRepository extends BaseRepository {
    private GithubApi githubApi;

    public GithubRepository() {
        githubApi = ServiceGenerator.createService(GithubApi.class);
    }

    public void getRepoList(final String name, final DataCallback dataCallback) {
        githubApi.userRepos(name).enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                dataCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                Timber.e(t, "#####  get repo list onFailure");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
