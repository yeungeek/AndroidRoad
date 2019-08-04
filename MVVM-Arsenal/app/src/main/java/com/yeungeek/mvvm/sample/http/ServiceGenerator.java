package com.yeungeek.mvvm.sample.http;


import com.yeungeek.mvvm.sample.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @date 2018/09/20
 */

public class ServiceGenerator {
    private static final String BASE_URL = "https://api.github.com/";

    private final static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ?
                    HttpLoggingInterceptor.Level.BASIC : HttpLoggingInterceptor.Level.NONE);

    private final static OkHttpClient httpClient =
            new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

    private final static Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    public final static <S> S createService(Class<S> clazz) {
        return retrofit.create(clazz);
    }
}
