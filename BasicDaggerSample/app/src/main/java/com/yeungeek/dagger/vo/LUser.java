package com.yeungeek.dagger.vo;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/03/08
 */

//@SType
public class LUser {
    public String login;
    public String avatarUrl;

    @Inject
    public LUser() {
        this.login = "yeungeek";
        this.avatarUrl = "http://avatar";
    }
}
