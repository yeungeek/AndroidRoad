package com.yeungeek.mvvm.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.yeungeek.mvvm.sample.mvvm.glass.GlassActivity;
import com.yeungeek.mvvm.sample.ui.GithubActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mGithubBtn;
    private Chronometer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGithubBtn = findViewById(R.id.github_btn);
        mGithubBtn.setOnClickListener(this);

        mTimer = findViewById(R.id.glass_timer);

        findViewById(R.id.glass_btn).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.github_btn:
                startActivity(new Intent(this, GithubActivity.class));
                break;
            case R.id.glass_btn:
                startActivity(new Intent(this, GlassActivity.class));
                break;
        }
    }
}
