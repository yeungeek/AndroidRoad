package com.yeungeek.archtraning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yeungeek.archtraning.base.FragmentUtil;
import com.yeungeek.archtraning.base.MainFragment;

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
}
