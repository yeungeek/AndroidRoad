package com.yeungeek.basicjava.proxy.dynamic;

public class Whisky implements Wine{
    @Override
    public void sell() {
        System.out.println("sell whisky");
    }
}
