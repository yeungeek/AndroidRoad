package com.yeungeek.dagger.vo.scope;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/03/08
 */

public class S1User {
    public String login;
    public String avatarUrl;

    @Inject
    public S1User() {
        this.login = "login-S1User";
        this.avatarUrl = "http://avatr.com-s1user";
    }

//    @Override
//    public String toString() {
//        return "S1User{" +
//                "login='" + login + '\'' +
//                ", avatarUrl='" + avatarUrl + '\'' +
//                '}' + this;
//    }
}
