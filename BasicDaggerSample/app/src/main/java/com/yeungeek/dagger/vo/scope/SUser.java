package com.yeungeek.dagger.vo.scope;

import com.yeungeek.dagger.di.SType;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/03/03
 */

@SType
public class SUser {
    public String login;
    public String avatarUrl;

    @Inject
    public SUser() {
        this.login = "login";
        this.avatarUrl = "http://avatr.com";
    }

//    @Override
//    public String toString() {
//        return "SUser{" +
//                "login='" + login + '\'' +
//                ", avatarUrl='" + avatarUrl + '\'' +
//                '}' + this;
//    }
}
