package com.yeungeek.retrofit.model;

/**
 * @author yangjian
 * @date 2018/08/28
 */

public class Repo {
    public String name;
    public String url;
    public String created;
    public String avatar;

    @Override
    public String toString() {
        return "Repo{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", created='" + created + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
