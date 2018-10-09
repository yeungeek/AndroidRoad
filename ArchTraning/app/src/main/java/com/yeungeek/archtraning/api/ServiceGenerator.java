package com.yeungeek.archtraning.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * @author yangjian
 * @date 2018/09/20
 */

public class ServiceGenerator {
    private static final String BASE_URL = "https://api.github.com/";
    public static final String GANK_URL = "https://gank.io/api/";

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
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();

    public final static <S> S createService(Class<S> clazz) {
        return retrofit.create(clazz);
    }
}
