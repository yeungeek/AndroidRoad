package com.yeungeek.dagger.vo;

import java.util.Set;

import javax.inject.Inject;

/**
 * @author yangjian
 * @date 2018/03/08
 */

public class SetUsers {
    Set<SetUser> users;

    @Inject
    public SetUsers(Set<SetUser> set) {
        this.users = set;
    }

    @Override
    public String toString() {
        return "SetUsers{" +
                "users=" + users +
                '}';
    }
}
