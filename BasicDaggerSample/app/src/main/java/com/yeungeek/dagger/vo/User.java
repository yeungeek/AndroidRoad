package com.yeungeek.dagger.vo;

import javax.inject.Inject;

/**
 * User
 *
 * @author yangjian
 * @date 2018/02/23
 */

public class User {
    public String login;
    public String avatarUrl;

    @Inject
    public User() {
        this.login = "yeungeek";
        this.avatarUrl = "http://avatar";
    }
}
