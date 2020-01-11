package com.yeungeek.dagger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yeungeek.dagger.R;
import com.yeungeek.dagger.di.DaggerInjectActivityComponent;
import com.yeungeek.dagger.vo.IUser;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/02/23
 */

public class InjectActivity extends AppCompatActivity {
    @Inject
    IUser user;

    private TextView mTvDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);

        DaggerInjectActivityComponent.create().inject(this);

        mTvDisplay = findViewById(R.id.tv_display);
        mTvDisplay.setText(user.login);
    }
}
