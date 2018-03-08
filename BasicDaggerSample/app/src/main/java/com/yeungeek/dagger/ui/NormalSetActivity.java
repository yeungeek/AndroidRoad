package com.yeungeek.dagger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yeungeek.dagger.R;
import com.yeungeek.dagger.di.DaggerNormalSetComponent;
import com.yeungeek.dagger.di.NormalSetComponent;
import com.yeungeek.dagger.vo.SetUser;
import com.yeungeek.dagger.vo.SetUsers;

import java.util.Set;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/03/08
 */

public class NormalSetActivity extends AppCompatActivity {
    private TextView tvDisplay;

    @Inject
    SetUsers setUsers;

    @Inject
    Set<SetUser> mSUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);

        tvDisplay = findViewById(R.id.tv_display);
        NormalSetComponent component = DaggerNormalSetComponent.create();
        component.inject(this);

        Set<SetUser> users = component.users();

        tvDisplay.setText("----> setUsers: " + setUsers + "\n ----> set: " + mSUsers + "" +
                "\n---> users: " + users);
    }
}
