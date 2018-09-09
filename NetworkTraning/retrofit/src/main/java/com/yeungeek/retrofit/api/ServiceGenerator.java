package com.yeungeek.retrofit.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yangjian
 * @date 2018/08/27
 */

public class ServiceGenerator {
    private final static String BASE_URL = "https://easy-mock.com/mock/5b3dd55353213d0beaa7cdf2/retrofit/";

    private final static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);

    private final static OkHttpClient httpClient =
            new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

    private final static Retrofit retrofit =
            new Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


    public final static <S> S createService(Class<S> clazz) {
        return retrofit.create(clazz);
    }
}
