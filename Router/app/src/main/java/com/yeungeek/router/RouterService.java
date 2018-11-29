package com.yeungeek.router;

import com.yeungeek.router.annotation.RouterUri;

/**
 * @author yangjian
 * @date 2018/11/16
 */

public interface RouterService {

    @RouterUri(value = "router://com.yeungeek.router.firstactivity")
    void startFirstActivity();
}
