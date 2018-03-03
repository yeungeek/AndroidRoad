package com.yeungeek.dagger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yeungeek.dagger.R;
import com.yeungeek.dagger.di.DaggerProvidesActivityComponent;
import com.yeungeek.dagger.vo.PUser;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/02/23
 */

public class ProvidesActivity extends AppCompatActivity {
    private TextView mTvDisplay;

    @Inject
    PUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);

        mTvDisplay = findViewById(R.id.tv_display);

        DaggerProvidesActivityComponent.create().inject(this);
        mTvDisplay.setText(user.avatarUrl);
//        DaggerProvidesActivityComponent component = DaggerProvidesActivityComponent.builder();
//        component.inject(this);
    }
}
