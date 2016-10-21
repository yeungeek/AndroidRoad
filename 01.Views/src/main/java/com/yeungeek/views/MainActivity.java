package com.yeungeek.views;

import com.yeungeek.views.base.BaseActivity;
import com.yeungeek.views.flow.FlowFragment;
import com.yeungeek.views.util.FragmentUtil;

import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    private FragmentUtil fragmentUtil;

    @Override
    protected void init() {
        super.init();
        fragmentUtil = new FragmentUtil(this, R.id.view_container);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.view_draw)
    void viewDraw() {
        Timber.d("----> click draw");
        fragmentUtil.switchTo(FlowFragment.class);
    }

    @OnClick(R.id.view_custom)
    void viewCustom() {

    }
}
