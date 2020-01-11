package com.yeungeek.archtraning.api;

import java.util.List;

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
}
