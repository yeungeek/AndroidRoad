package com.yeungeek.dagger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yeungeek.dagger.R;
import com.yeungeek.dagger.di.DaggerSingletonComponent;
import com.yeungeek.dagger.vo.scope.SingletonUser;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/03/08
 */

public class SingletonActivity extends AppCompatActivity {
    private TextView tvDisplay;

    @Inject
    SingletonUser s1;

    @Inject
    SingletonUser s2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);

        DaggerSingletonComponent.create().inject(this);
        tvDisplay = findViewById(R.id.tv_display);

        tvDisplay.setText("s1: " + s1 + "\ns2: " + s2);
    }
}
