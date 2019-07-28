package com.yeungeek.basicjava.proxy.stat;

import com.yeungeek.basicjava.proxy.Movie;

public class RealMovie implements Movie {
    @Override
    public void play() {
        System.out.println("real play movie");
    }
}
