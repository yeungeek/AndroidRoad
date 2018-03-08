package com.yeungeek.dagger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yeungeek.dagger.R;
import com.yeungeek.dagger.di.DaggerMapComponent;
import com.yeungeek.dagger.di.MapComponent;
import com.yeungeek.dagger.vo.SetUser;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/03/08
 */

public class MapActivity extends AppCompatActivity {
    private TextView tvDisplay;

    @Inject
    Map<String, SetUser> userMap;

    @Inject
    Map<Class<?>, SetUser> classMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inject);

        tvDisplay = findViewById(R.id.tv_display);

        MapComponent component = DaggerMapComponent.create();
        component.inject(this);

        tvDisplay.setText("---> map: " + userMap + "\n classMap: " + classMap);
    }
}
