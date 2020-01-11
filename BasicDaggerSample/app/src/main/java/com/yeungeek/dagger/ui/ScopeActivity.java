package com.yeungeek.dagger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yeungeek.dagger.R;
import com.yeungeek.dagger.di.DaggerScopeComponent;
import com.yeungeek.dagger.vo.scope.S1User;
import com.yeungeek.dagger.vo.scope.SUser;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/03/08
 */

public class ScopeActivity extends AppCompatActivity {
    private TextView tvDisplay;

    @Inject
    SUser s1;
    @Inject
    SUser s2;

    @Inject
    S1User ss1;
    @Inject
    S1User ss2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);

        DaggerScopeComponent.create().inject(this);

        tvDisplay = findViewById(R.id.tv_display);

        tvDisplay.setText("s1: " + s1 + "\ns2: " + s2 +
                "\nss1: " + ss1 + "\nss2: " + ss2);
    }

    //    @Override
//    public void setContentView(View view) {
//        super.setContentView(view);
//        setContentView(R.layout.activity_inject);
//
//        tvDisplay = findViewById(R.id.tv_display);
//
//        tvDisplay.setText("s1: " + s1);
//    }
}
