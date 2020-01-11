package com.yeungeek.basicjava.data.list;

import java.util.ArrayList;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        List<Test> list1 = new ArrayList<>();
        list1.add(new Test().setPackageName("com.test1"));
        list1.add(new Test().setPackageName("com.test2"));
        list1.add(new Test().setPackageName("com.test3"));
        list1.add(new Test().setPackageName("com.test4"));
        list1.add(new Test().setPackageName("com.test5"));
        list1.add(new Test().setPackageName("com.test6"));

        List<Test> list2 = new ArrayList<>();
        list2.add(new Test().setPackageName("com.test1"));
        list2.add(new Test().setPackageName("com.test4"));
        list2.add(new Test().setPackageName("com.test5"));

        list1.removeAll(list2);

        for (Test t : list1) {
            System.out.println("package:" + t.getPackageName());
        }
    }

    static class Test {
        private String packageName;

        public String getPackageName() {
            return packageName;
        }

        public Test setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Test test = (Test) o;

            return packageName.equals(test.packageName);
        }

        @Override
        public int hashCode() {
            return packageName.hashCode();
        }
    }
}
