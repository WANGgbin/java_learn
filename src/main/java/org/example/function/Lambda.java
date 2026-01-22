package org.example.function;

import java.util.Arrays;

/*
*
* 1. 解决什么问题
*
*
* 2. 使用方式
*
* 3. 实现原理
*
* 不管是 lambda 还是方法引用(静态方法、实例方法)，底层都是由 jvm 动态创建了一个 InstanceKlass，该 InstanceKlass 的方法表
* 就包含一个方法，_implemented_interfaces 指向对应接口的 InterfaceKlass.
*
* 这个方法简单理解就是对于 lambda 或者方法引用的封装(底层通过 invokedynamic 创建对应的实例)。
*
* 4. 注意事项
*
*
* */

public class Lambda {
    public static void main(String[] args) {
        String[] array = new String[] { "Apple", "Orange", "Banana", "Lemon" };

        // 可以传入方法引用.
        Arrays.sort(array, Lambda::cmp);
        System.out.println(String.join(", ", array));

        // 也可以传入 lambda
        Arrays.sort(array, (s1, s2) -> s1.compareTo(s2));
        System.out.println(String.join(", ", array));
    }

    static int cmp(String s1, String s2) {
        return s1.compareTo(s2);
    }
}
