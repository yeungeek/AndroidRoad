package com.yeungeek.dagger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yeungeek.dagger.ui.InjectActivity;

/**
 * @author yangjian
 * @date 2018/02/23
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnInject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnInject = findViewById(R.id.btn_inject);
        mBtnInject.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_inject:
                startActivity(new Intent(MainActivity.this, InjectActivity.class));
                break;
            default:
                break;
        }
    }
}
