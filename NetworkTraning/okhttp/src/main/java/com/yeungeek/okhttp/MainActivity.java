package com.yeungeek.okhttp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yeungeek.okhttp.socket.SocketActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String ENDPOINT = "https://www.wanandroid.com";
    private OkHttpClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mClient = new OkHttpClient
                .Builder()
//                .addInterceptor(new NetworkInterceptor())
                .addNetworkInterceptor(new NetworkInterceptor())
                .build();
        findViewById(R.id.sync_btn).setOnClickListener(this);
        findViewById(R.id.async_btn).setOnClickListener(this);
        findViewById(R.id.socket_btn).setOnClickListener(this);
    }

    private void urlConnection() {
        try {
            URL url = new URL(ENDPOINT + "/banner/json/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            Log.d("DEBUG", "##### response code: " + code);
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, length);
                }

                String result = bos.toString(StandardCharsets.UTF_8.name());
                bos.close();
                is.close();

                Log.d("DEBUG", "##### response result: " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncMethod() {
        Request request = new Request.Builder()
                .url(ENDPOINT + "/banner/json/")
                .build();

        try {
            Response response = mClient.newCall(request).execute();
            Log.d("DEBUG", "##### response: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void asyncMethod() {
//        Request request = new Request.Builder()
//                .get()
//                .url(ENDPOINT + "/wxarticle/chapters/json")
//                .build();

        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DEBUG", "##### onFailure: ", e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("DEBUG", "##### response thread: " + Thread.currentThread().getId());
                Log.d("DEBUG", "##### response: " + response.body().string());

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sync_btn:
                Log.d("DEBUG", "##### click sync");
                new Thread(() -> {
//                    syncMethod();
                    urlConnection();
                }).start();
                break;
            case R.id.async_btn:
                asyncMethod();
                break;
            case R.id.socket_btn:
                startActivity(new Intent(MainActivity.this, SocketActivity.class));
                break;
        }
    }
}
