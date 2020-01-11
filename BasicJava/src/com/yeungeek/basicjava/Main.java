package com.yeungeek.basicjava;

import com.yeungeek.basicjava.proxy.Movie;
import com.yeungeek.basicjava.proxy.dynamic.*;
import com.yeungeek.basicjava.proxy.stat.Cinema;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========");

//        testStaticProxy();
        testDynamic();
    }

    private static void testStaticProxy() {
        Movie movie = new Cinema();
        movie.play();
    }

    private static void testDynamic() {
        Maotai maotai = new Maotai();
        InvocationHandler platform = new SellPlatform(maotai);
        Wine dynamicWine = (Wine) Proxy.newProxyInstance(maotai.getClass().getClassLoader(),
                maotai.getClass().getInterfaces(), platform);
        dynamicWine.sell();

        Whisky whisky = new Whisky();
        InvocationHandler wp = new SellPlatform(whisky);

        Wine dw = (Wine) Proxy.newProxyInstance(whisky.getClass().getClassLoader(),
                whisky.getClass().getInterfaces(), wp);
        dw.sell();


        Apple apple = new Apple();
        InvocationHandler appleI = new SellPlatform(apple);
        Fruit appleF = (Fruit) Proxy.newProxyInstance(apple.getClass().getClassLoader(),
                apple.getClass().getInterfaces(), appleI);
        appleF.name();

        System.out.println("dynamic proxy class: " + dynamicWine.getClass().getName());
        System.out.println("dynamic proxy class: " + dw.getClass().getName());
        System.out.println("dynamic proxy class: " + appleF.getClass().getName());
    }
}
