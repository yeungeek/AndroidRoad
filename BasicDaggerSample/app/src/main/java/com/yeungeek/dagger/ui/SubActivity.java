package com.yeungeek.dagger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yeungeek.dagger.R;
import com.yeungeek.dagger.di.DaggerParent1Component;
import com.yeungeek.dagger.di.Parent1Component;
import com.yeungeek.dagger.di.Sub1AppComponent;
import com.yeungeek.dagger.vo.SetUser;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author yangjian
 * @date 2018/03/08
 */

public class SubActivity extends AppCompatActivity {
    private TextView tvDisplay;

//    @Inject
//    SetUser subUser;
//
//    @Inject
//    QUser parentUser;

    @Inject
    Provider<Sub1AppComponent.Builder> builderProvider;
    Sub1AppComponent subAppComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);


        tvDisplay = findViewById(R.id.tv_display);

//        ParentComponent parentComponent = DaggerParentComponent.create();
//        SubAppComponent subComponent = DaggerSubAppComponent.builder().parentComponent(parentComponent).build();
//        subComponent.inject(this);
//
//        QUser user = parentComponent.supplyQUser();
//
//        tvDisplay.setText("---> sub user: " + subUser + "\n" + "parent: " + parentUser + parentUser.hashCode()
//                + "\n supply: " + user + user.hashCode());

        Parent1Component component = DaggerParent1Component.create();
        component.inject(this);

        subAppComponent = builderProvider.get().build();
        SetUser setUser = subAppComponent.supplySetUser();

        tvDisplay.setText("---> sub user: " + setUser);
    }
}
