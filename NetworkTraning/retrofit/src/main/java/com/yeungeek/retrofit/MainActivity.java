package com.yeungeek.retrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yeungeek.retrofit.api.MockAPI;
import com.yeungeek.retrofit.api.ServiceGenerator;
import com.yeungeek.retrofit.model.Repo;
import com.yeungeek.retrofit.model.SingleRepo;
import com.yeungeek.retrofit.model.WrapRepo;

import androidx.appcompat.app.AppCompatActivity;

import org.reactivestreams.Subscriber;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mGet;
    private MockAPI api;
    private Callback<WrapRepo> callback;
    private Callback<SingleRepo> repoCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = ServiceGenerator.createService(MockAPI.class);

        initViews();
    }

    private void initViews() {
        mGet = findViewById(R.id.get);
        mGet.setOnClickListener(this);

        findViewById(R.id.get_path).setOnClickListener(this);
        findViewById(R.id.post).setOnClickListener(this);
        findViewById(R.id.post_form).setOnClickListener(this);
        findViewById(R.id.post_path).setOnClickListener(this);
        findViewById(R.id.put_path).setOnClickListener(this);
        findViewById(R.id.delete_path).setOnClickListener(this);
        findViewById(R.id.patch_path).setOnClickListener(this);
        findViewById(R.id.post_http).setOnClickListener(this);

        callback = new Callback<WrapRepo>() {
            @Override
            public void onResponse(Call<WrapRepo> call, Response<WrapRepo> response) {
                Log.d("debug", "----> " + response.body());
            }

            @Override
            public void onFailure(Call<WrapRepo> call, Throwable t) {
                Log.e("debug", "----> " + t.getMessage(), t);
            }
        };

        repoCallback = new Callback<SingleRepo>() {
            @Override
            public void onResponse(Call<SingleRepo> call, Response<SingleRepo> response) {
                Log.d("debug", "----> " + response.body());
            }

            @Override
            public void onFailure(Call<SingleRepo> call, Throwable t) {
                Log.e("debug", "----> " + t.getMessage(), t);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:
                get();
                break;
            case R.id.get_path:
                getPath();
                break;
            case R.id.post:
                post();
                break;
            case R.id.post_form:
                postForm();
                break;
            case R.id.post_path:
                postPath();
                break;
            case R.id.put_path:
                putPath();
                break;
            case R.id.delete_path:
                deletePath();
                break;
            case R.id.patch_path:
                patchPath();
                break;
            case R.id.post_http:
                postHttp();
                break;
        }
    }

    private void get() {
        final Call<WrapRepo> call = api.query("squareup");
        call.enqueue(callback);
//        api.query("squareup").subscribeOn(Schedulers.io())
//                .observeOn().subscribe();
    }

    private void getPath() {
//        final Call<WrapRepo> call = api.queryPath("yeungeek");
//        call.enqueue(callback);
        api.queryPath("yeungeek").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WrapRepo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WrapRepo wrapRepo) {
                        Log.d("DEBUG", "##### " + wrapRepo);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void post() {
        final Call<SingleRepo> call = api.postQuery("yeungeek");
        call.enqueue(repoCallback);
    }

    private void postForm() {
        final Call<SingleRepo> call = api.postForm("http://yeungeek.com/主页");
        call.enqueue(repoCallback);
    }

    private void postPath() {
        final Call<SingleRepo> call = api.postPath("single");
        call.enqueue(repoCallback);
    }

    private void putPath() {
        final Call<SingleRepo> call = api.putPath("yeungeek");
        call.enqueue(repoCallback);
    }

    private void deletePath() {
        final Call<SingleRepo> call = api.deletePath("yeungeek");
        call.enqueue(repoCallback);
    }

    private void patchPath() {
        final Call<SingleRepo> call = api.patchPath("yeungeek");
        call.enqueue(repoCallback);
    }

    private void postHttp() {
        final Call<SingleRepo> call = api.postHttp("yeungeek");
        call.enqueue(repoCallback);
    }
}
