package com.yeungeek.modulepay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yeungeek.library.router.Consts;
import com.yeungeek.library.router.RouterUtils;

/**
 * @author yangjian
 * @date 2018/09/28
 */
@Route(path = Consts.MODULE_PAY_MAIN)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mToModuleA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modulepay_activity_main);

        mToModuleA = findViewById(R.id.module_a);
        mToModuleA.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.module_a) {
            RouterUtils.navigation(Consts.MODULEA_MAIN);
        }
    }
}
