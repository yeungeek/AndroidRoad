package com.yeungeek.dagger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yeungeek.dagger.R;
import com.yeungeek.dagger.di.DaggerLazyComponent;
import com.yeungeek.dagger.vo.LUser;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.Lazy;

/**
 * @author yangjian
 * @date 2018/03/08
 */

public class LazyActivity extends AppCompatActivity {
    private TextView tvDisplay;

    @Inject
    Lazy<LUser> mUser;

    @Inject
    Provider<LUser> mPUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);

        tvDisplay = findViewById(R.id.tv_display);

        DaggerLazyComponent.create().inject(this);

        LUser user = mUser.get();
        LUser user1 = mUser.get();

        LUser p1 = mPUser.get();
        LUser p2 = mPUser.get();
        tvDisplay.setText("---> user: " + user + ", \n user1: " + user1 + "" +
                "\n p1: " + p1 + ",\np2: " + p2);
    }
}
