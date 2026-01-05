package org.example.annoymousclass;

import org.example.util.UtilPerson;

public class Closure {
    public static void main(String[] args) {
        int varInt = 6;
        String varString = "Hello";

        // 匿名类派生自 UtilBase 类，后面的 () 表示调用对应的构造函数
        // 如果是无参构造函数，可以省略 ()
        UtilPerson util = new UtilPerson("base") {
            @Override
            public void doWork() {
                System.out.println(String.format("[%s] %s works", this.getClass().getName(), varString));
            }
        };

        util.doWork();
    }
}
