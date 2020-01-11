package com.yeungeek.modulea;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yeungeek.library.router.Consts;
import com.yeungeek.library.router.RouterUtils;
import com.yeungeek.library.util.FragmentUtil;

/**
 * @author yangjian
 * @date 2018/09/28
 */
@Route(path = Consts.MODULEA_MAIN)
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_activity_main);
        FragmentUtil.pushFragmentToBackStack(getSupportFragmentManager(), RouterUtils.getFragment(Consts.MODULEA_MVP_FRAGMENT), R.id.content_fragment);
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
