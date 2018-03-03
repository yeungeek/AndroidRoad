package com.yeungeek.dagger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yeungeek.dagger.R;

/**
 * @author yangjian
 * @date 2018/02/23
 */

public class QualifierActivity extends AppCompatActivity {
    private TextView mTvDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);

        mTvDisplay = findViewById(R.id.tv_display);
        
    }
}
