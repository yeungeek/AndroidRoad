package com.yeungeek.library.http.retrofit2;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yangjian
 * @date 2018/09/20
 */

public class ServiceGenerator {
    //TODO may be dynamic changed
    private static final String BASE_URL = "https://api.github.com/";

    private final static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);

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
