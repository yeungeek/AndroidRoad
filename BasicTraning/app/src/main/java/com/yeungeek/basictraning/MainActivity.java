package com.yeungeek.basictraning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yeungeek.basictraning.fragments.FragmentUtil;
import com.yeungeek.basictraning.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentUtil.pushFragmentToBackStack(getSupportFragmentManager(), MainFragment.class, R.id.content_fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
