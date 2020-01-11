package com.yeungeek.library.data.model;


/**
 * @author yangjian
 * @date 2018/09/20
 */

public class Repo{
    public int id;
    public String name;

    public int getId() {
        return id;
    }

    public Repo setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Repo setName(String name) {
        this.name = name;
        return this;
    }
}
