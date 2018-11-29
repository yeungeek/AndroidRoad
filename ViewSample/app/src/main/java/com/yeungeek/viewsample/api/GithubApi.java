package com.yeungeek.viewsample.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author yangjian
 * @date 2018/09/20
 */

public interface GithubApi {
    @GET("users/{username}/repos")
    Call<List<Repo>> userRepos(@Path("username") String username);

    @GET("users/{username}/repos")
    Observable<List<Repo>> obsRespo(@Path("username") String username);
}
