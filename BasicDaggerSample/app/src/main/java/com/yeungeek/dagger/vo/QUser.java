package com.yeungeek.dagger.vo;

/**
 * @author yangjian
 * @date 2018/02/23
 */

public class QUser {
    public String type;
    public String login;
    public String avatarUrl;

    public QUser() {

    }

    public QUser(String type) {
        this.type = type;
        this.login = "username";
        this.avatarUrl = "http://www.baidu.com";
    }

    @Override
    public String toString() {
        return "QUser{" +
                "type='" + type + '\'' +
                ", login='" + login + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
