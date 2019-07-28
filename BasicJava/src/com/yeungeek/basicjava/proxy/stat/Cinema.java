package com.yeungeek.basicjava.proxy.stat;

import com.yeungeek.basicjava.proxy.Movie;

public class Cinema implements Movie {
    private RealMovie realMovie;

    public Cinema() {
        super();
        this.realMovie = new RealMovie();
    }

    @Override
    public void play() {
        beforePlay();
        realMovie.play();
        afterPlay();
    }

    private void beforePlay() {
        System.out.println("before play");
    }

    private void afterPlay() {
        System.out.println("after play");
    }
}
