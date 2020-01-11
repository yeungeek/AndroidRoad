package com.yeungeek.okhttp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String ENDPOINT = "https://www.wanandroid.com";

    private OkHttpClient okHttpClient;
    private Map<String, List<Cookie>> mCookieStore;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void setup() {
        System.out.println("setup");
        okHttpClient = new OkHttpClient();
        mCookieStore = new HashMap<>();
    }

    @Test
    public void testGet() throws Exception {
        Request request = new Request.Builder()
                .url(ENDPOINT + "/banner/json/")
                .get().build();

        Response response = okHttpClient.newCall(request).execute();
        assertTrue(response.isSuccessful());

        System.out.println("Header: " + response.headers());
        System.out.println(response.body().string());
    }

    @Test
    public void testGetGank() throws IOException {
        Request request = new Request.Builder()
                .url("http://gank.io/api/today")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        assertTrue(response.isSuccessful());

        System.out.println("Header: " + response.headers());
        System.out.println(response.body().string());
    }

    @Test
    public void testPost() throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("username", "yeungeek")
                .add("password", "")
                .build();

        Request request = new Request.Builder()
                .url(ENDPOINT + "/user/login")
                .post(body).build();

        OkHttpClient newOkHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        System.out.println("cookies url: " + url.toString());
                        for (Cookie cookie : cookies) {
                            System.out.println("cookies: " + cookie.toString());
                        }

                        mCookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List list = mCookieStore.get(url.host());
                        return null == list ? new ArrayList<Cookie>() : list;
                    }
                })
                .build();

        Response response = newOkHttpClient.newCall(request).execute();
        assertTrue(response.isSuccessful());

        System.out.println("Header: " + response.headers());
        System.out.println(response.body().string());

//        testGetList();
    }


    @Test
    public void testGetList() throws IOException {
        Request request = new Request.Builder()
                .url(ENDPOINT + "/lg/collect/list/0/json")
                .addHeader("Cookie", "JSESSIONID=md5; path=/; secure; httponly;loginUserName=yeungeek; expires=Sun, 23 Jun 2019 07:52:20 GMT; path=/;" +
                        "token_pass=md5; expires=Sun, 23 Jun 2019 07:52:20 GMT; path=/")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        assertTrue(response.isSuccessful());

        System.out.println("Header: " + response.headers());
        System.out.println(response.body().string());
    }

    @Test
    public void testAsync() {
        Request request = new Request.Builder()
                .url(ENDPOINT + "/banner/json/")
                .get().build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("async method: " + response.body().string());
            }
        });
    }


    @After
    public void tearDown() {
        System.out.println("tearDown");
        okHttpClient = null;
    }

}