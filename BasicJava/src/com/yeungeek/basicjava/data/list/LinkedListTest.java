package com.yeungeek.basicjava.data.list;

import java.util.LinkedList;
import java.util.List;

public class LinkedListTest {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        println(list);
        System.out.println(list.removeLast());
        println(list);
        list.add(6);
        println(list);
    }

    private static void println(List<Integer> list) {
        for (Integer i : list) {
            System.out.println(i);
        }
    }
}
