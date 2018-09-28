package com.yeungeek.library.router;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author yangjian
 * @date 2018/09/28
 */

public interface ToastService extends IProvider {
    /**
     * toast
     * @param message
     */
    void toast(String message);
}
