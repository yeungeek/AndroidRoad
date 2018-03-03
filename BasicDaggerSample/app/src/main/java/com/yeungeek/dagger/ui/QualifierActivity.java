package com.yeungeek.dagger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yeungeek.dagger.R;
import com.yeungeek.dagger.di.DaggerQualifierActivityComponent;
import com.yeungeek.dagger.di.QualifierModule;
import com.yeungeek.dagger.di.Type;
import com.yeungeek.dagger.vo.PUser;
import com.yeungeek.dagger.vo.QUser;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/02/23
 */

public class QualifierActivity extends AppCompatActivity {
    private TextView mTvDisplay;

    @Type("boy")
    @Inject
    QUser boyUser;

    @Type("girl")
    @Inject
    QUser girlUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);

        mTvDisplay = findViewById(R.id.tv_display);

        DaggerQualifierActivityComponent.create().inject(this);
//        DaggerQualifierActivityComponent.builder().qualifierModule(new QualifierModule()).build().inject(this);
        mTvDisplay.setText("----> " + boyUser.toString() + ", girl: " + girlUser);
    }
}
