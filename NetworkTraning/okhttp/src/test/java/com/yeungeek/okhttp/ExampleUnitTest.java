package com.yeungeek.okhttp;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String ENDPOINT = "https://api.github.com/repos/square/okhttp/contributors";
    private final static String BASE_URL = "https://easy-mock.com/mock/5b3dd55353213d0beaa7cdf2/retrofit/";

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testGet() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(ENDPOINT).build();
        Response response = client.newCall(request).execute();

        Headers headers = response.headers();
        System.out.println(headers.toString());

        String respStr = response.body().string();
        System.out.println(respStr);
        assertNotNull(respStr);

        System.out.println("Thread: " + Thread.currentThread().getName());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Thread enqueue: " + Thread.currentThread().getName());
                System.out.println(response.body().string());
            }
        });
    }

    @Test
    public void testPost() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody body = new FormBody.Builder()
                .add("id", "yeungeek")
                .build();

        Request request = new Request.Builder().url(BASE_URL + "user")
                .post(body).build();

        Response response = client.newCall(request).execute();
        assertTrue(response.isSuccessful());

        System.out.println(response.body().string());
    }
}