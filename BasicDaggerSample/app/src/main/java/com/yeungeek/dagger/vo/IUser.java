package com.yeungeek.dagger.vo;

import javax.inject.Inject;

/**
 * IUser
 *
 * @author yangjian
 * @date 2018/02/23
 */

public class IUser {
    public String login;
    public String avatarUrl;

    @Inject
    public IUser() {
        this.login = "yeungeek";
        this.avatarUrl = "http://avatar";
    }
}
