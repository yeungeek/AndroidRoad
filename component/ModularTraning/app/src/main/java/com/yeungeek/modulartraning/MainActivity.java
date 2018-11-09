package com.yeungeek.modulartraning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yeungeek.library.util.FragmentUtil;
import com.yeungeek.modulartraning.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View view = new PageBlock.Builder()
//                .setPageId(1000)
//                .setFactory(new PageBlock.TypeFactory() {
//                    @Override
//                    public void getList(String pageName) {
//                        Log.d("DEBUG", "====== typeFactory: " + pageName);
//                    }
//                })
//                .build(this);
//        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT));
//        setContentView(view);
        setContentView(R.layout.app_activity_main);
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
