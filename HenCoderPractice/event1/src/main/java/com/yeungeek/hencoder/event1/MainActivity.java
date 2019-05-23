package com.yeungeek.hencoder.event1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yeungeek.hencoder.event1.view.CustomParentView;
import com.yeungeek.hencoder.event1.view.CustomSubView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomParentView mParentView;
    private CustomSubView mSubView;
    private Button mDisallow;
    private boolean mIsDisallow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mParentView = findViewById(R.id.parent_view);
        mSubView = findViewById(R.id.sub_view);
        mDisallow = findViewById(R.id.disallow_btn);

        mDisallow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.disallow_btn:
                mIsDisallow = !mIsDisallow;
                mParentView.requestDisallowInterceptTouchEvent(mIsDisallow);
                break;
        }
    }
}
