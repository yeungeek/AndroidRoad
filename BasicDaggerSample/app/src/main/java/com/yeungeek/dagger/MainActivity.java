package com.yeungeek.dagger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yeungeek.dagger.ui.InjectActivity;
import com.yeungeek.dagger.ui.LazyActivity;
import com.yeungeek.dagger.ui.ProvidesActivity;
import com.yeungeek.dagger.ui.QualifierActivity;
import com.yeungeek.dagger.ui.ScopeActivity;
import com.yeungeek.dagger.ui.SingletonActivity;

/**
 * @author yangjian
 * @date 2018/02/23
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnInject;
    private Button mBtnProvide;
    private Button mBtnQualifier;
    private Button mBtnScope;
    private Button mBtnSingleton;
    private Button mBtnLazy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnInject = findViewById(R.id.btn_inject);
        mBtnInject.setOnClickListener(this);

        mBtnProvide = findViewById(R.id.btn_provide);
        mBtnProvide.setOnClickListener(this);

        mBtnQualifier = findViewById(R.id.btn_qualifier);
        mBtnQualifier.setOnClickListener(this);

        mBtnScope = findViewById(R.id.btn_scope);
        mBtnScope.setOnClickListener(this);


        mBtnSingleton = findViewById(R.id.btn_singleton);
        mBtnSingleton.setOnClickListener(this);

        mBtnLazy = findViewById(R.id.btn_lazy);
        mBtnLazy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_inject:
                startActivity(new Intent(MainActivity.this, InjectActivity.class));
                break;
            case R.id.btn_provide:
                startActivity(new Intent(MainActivity.this, ProvidesActivity.class));
                break;
            case R.id.btn_qualifier:
                startActivity(new Intent(MainActivity.this, QualifierActivity.class));
                break;
            case R.id.btn_scope:
                startActivity(new Intent(MainActivity.this, ScopeActivity.class));
                break;
            case R.id.btn_singleton:
                startActivity(new Intent(MainActivity.this, SingletonActivity.class));
                break;
            case R.id.btn_lazy:
                startActivity(new Intent(MainActivity.this, LazyActivity.class));
                break;
            default:
                break;
        }
    }
}
