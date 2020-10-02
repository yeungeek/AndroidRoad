package com.yeungeek.videosample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yeungeek.videosample.opengl.SimpleRenderActivity;

import java.util.ArrayList;

public class TestJava extends AppCompatActivity {
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TestJava.this, SimpleRenderActivity.class));
            }
        });
    }

    public void test() {
        ArrayList<Pair<String, Class<?>>> datas = new ArrayList();
    }
}
