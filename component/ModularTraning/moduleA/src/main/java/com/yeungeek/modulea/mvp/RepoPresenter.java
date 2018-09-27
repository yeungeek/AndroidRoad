package com.yeungeek.modulea.mvp;


import com.yeungeek.library.data.model.Repo;
import com.yeungeek.library.http.retrofit2.ServiceGenerator;
import com.yeungeek.modulea.api.GithubApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yangjian
 * @date 2018/09/21
 */

public class RepoPresenter {
    private RepoView mView;
    private GithubApi api;

    public RepoPresenter(RepoView view) {
        this.mView = view;
        this.api = ServiceGenerator.createService(GithubApi.class);
    }

    public void getRepo(final String username) {
        api.userRepos(username).enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                mView.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {

            }
        });
    }
}
