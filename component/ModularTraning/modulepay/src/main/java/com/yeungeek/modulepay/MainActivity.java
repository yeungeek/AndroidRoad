package com.yeungeek.modulepay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yeungeek.library.router.Consts;
import com.yeungeek.library.router.RouterUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yangjian
 * @date 2018/09/28
 */
@Route(path = Consts.MODULE_PAY_MAIN)
public class MainActivity extends AppCompatActivity {
    @BindView(R2.id.module_a)
    Button aBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modulepay_activity_main);
        ButterKnife.bind(this);
//        mToModuleA = findViewById(R.id.module_a);
//        mToModuleA.setOnClickListener(this);
        aBtn.setText("bind button");
    }


    @OnClick(R2.id.module_a)
    void click() {
        RouterUtils.navigation(Consts.MODULEA_MAIN);
    }
//
//    @Override
//    public void onClick(View v) {
////        switch (v.getId()) {
////            case R2.id.module_a:
////                RouterUtils.navigation(Consts.MODULEA_MAIN);
////                break;
////            default:
////                break;
////        }
//        if (v.getId() == R.id.module_a) {
//            RouterUtils.navigation(Consts.MODULEA_MAIN);
//        }
//    }
}
