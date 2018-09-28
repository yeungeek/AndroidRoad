package com.yeungeek.modulepay.debug;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yeungeek.library.router.Consts;
import com.yeungeek.library.router.ToastService;

/**
 * @author yangjian
 * @date 2018/09/28
 */

@Route(path = Consts.PAY_SERVCIE)
public class PayToastImpl implements ToastService {
    private Context mContext;

    @Override
    public void init(Context context) {
        mContext = context;
    }

    @Override
    public void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
