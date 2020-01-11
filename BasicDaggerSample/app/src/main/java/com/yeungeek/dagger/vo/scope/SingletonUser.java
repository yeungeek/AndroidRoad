package com.yeungeek.dagger.vo.scope;

import javax.inject.Singleton;

/**
 * @author yangjian
 * @date 2018/03/08
 */

//@Singleton
public class SingletonUser {
    public String login;
    public String avatarUrl;

    public SingletonUser() {
        this.login = "login-S1User";
        this.avatarUrl = "http://avatr.com-s1user";
    }
}
