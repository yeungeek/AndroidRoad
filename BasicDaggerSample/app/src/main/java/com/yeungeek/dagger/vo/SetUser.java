package com.yeungeek.dagger.vo;

/**
 * @author yangjian
 * @date 2018/03/08
 */

public class SetUser {
    public String value;

    public SetUser(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SetUser{" +
                "value='" + value + '\'' +
                '}';
    }
}
