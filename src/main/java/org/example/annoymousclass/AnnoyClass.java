package org.example.annoymousclass;

import org.example.util.UtilPerson;

public class AnnoyClass {
    private int count;

    public void doWork() {
        String varString = "Hello";

        // 匿名类派生自 UtilBase 类，后面的 () 表示调用对应的构造函数
        // 如果是无参构造函数，可以省略 ()
        UtilPerson util = new UtilPerson("base") {
            @Override
            public void doWork() {
                System.out.println(String.format("[%s] works. varString:%s, count: %d", this.getClass().getName(), varString, count));
            }
        };

        // 输出的 count = 0
        util.doWork();

        count = 1;

        // 输出的 count = 1
        util.doWork();
    }
}
