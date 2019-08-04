package com.yeungeek.basicjava.reflect;

public class TestViewModel extends BaseViewModel<TestRepo> {
    @Override
    void vmMethod() {
        System.out.println("Test View Model");
    }
}
