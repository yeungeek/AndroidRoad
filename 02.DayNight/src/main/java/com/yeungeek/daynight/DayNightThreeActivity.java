package com.yeungeek.daynight;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yeungeek.daynight.m1.Colorful;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;

public class DayNightThreeActivity extends AppCompatActivity {
    @BindView(R.id.id_table_layout)
    TabLayout tableLayout;
    @BindView(R.id.id_view_pager)
    ViewPager viewPager;

    private Colorful mColorful;

    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_day_night_three);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tableLayout.setupWithViewPager(viewPager);

        mColorful = new Colorful.Builder(this)
//                .backgroundDrawable(R.id.root_view, R.attr.root_view_bg)
                .backgroundColor(R.id.id_table_layout, R.attr.rootViewBg)
                .textColor(R.id.id_change, R.attr.btnBg)
                .create(); // 设置文本颜色
    }

    @DebugLog
    @OnClick(R.id.id_change)
    public void change() {
        if (ChangeModeHelper.getChangeMode(this) == ChangeModeHelper.MODE_DAY) {
            ChangeModeHelper.setChangeMode(this, ChangeModeHelper.MODE_NIGHT);
            mColorful.setTheme(R.style.NightTheme);
        } else {
            ChangeModeHelper.setChangeMode(this, ChangeModeHelper.MODE_DAY);
            mColorful.setTheme(R.style.DayTheme);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return DayNightFragment.newInstance(0, "Page # 1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return DayNightFragment.newInstance(1, "Page # 2");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }

}
