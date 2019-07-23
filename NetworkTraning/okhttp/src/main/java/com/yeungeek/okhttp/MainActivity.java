package com.yeungeek.okhttp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

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


        mClient = new OkHttpClient();
        findViewById(R.id.sync_btn).setOnClickListener(this);
        findViewById(R.id.async_btn).setOnClickListener(this);
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
        Request request = new Request.Builder()
                .get()
                .url(ENDPOINT + "/wxarticle/chapters/json")
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DEBUG", "##### onFailure: ", e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
                    syncMethod();
                }).start();
                break;
            case R.id.async_btn:
                asyncMethod();
                break;
        }
    }
}
