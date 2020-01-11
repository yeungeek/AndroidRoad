package com.yeungeek.retrofit.model;

import java.util.List;

/**
 * @date 2018/08/28
 */

public class WrapRepo {
    public Inner data;

    public static class Inner {
        public List<Repo> repos;

        @Override
        public String toString() {
            return "Inner{" +
                    "repos=" + repos +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WrapRepo{" +
                "data=" + data +
                '}';
    }
}
