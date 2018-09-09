package com.yeungeek.retrofit.api;

import com.yeungeek.retrofit.model.SingleRepo;
import com.yeungeek.retrofit.model.WrapRepo;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @date 2018/08/27
 */

public interface MockAPI {
    @GET("query")
    Call<WrapRepo> query(@Query("name") String name);

    @GET("users/{user}/repos")
    Call<WrapRepo> queryPath(@Path("user") String user);

    @POST("user")
    Call<SingleRepo> postQuery(@Query("id") String id);

    @POST("user")
    @FormUrlEncoded
    Call<SingleRepo> postForm(@Field(value = "id", encoded = false) String id);

    @POST("user/{id}/info")
    Call<SingleRepo> postPath(@Path("id") String id);

    @PUT("user/following/{username}")
    Call<SingleRepo> putPath(@Path("username") String username);

    @DELETE("user/following/{username}")
    Call<SingleRepo> deletePath(@Path("username") String username);

    @PATCH("user/following/{username}")
    Call<SingleRepo> patchPath(@Path("username") String username);

    /**
     * other
     */
    @Headers({"User-Agent: Http/yeungeek"})
    @HTTP(method = "POST", path = "user", hasBody = true)
    @FormUrlEncoded
    Call<SingleRepo> postHttp(@Field("id") String id);
}
