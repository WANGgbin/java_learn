package org.example.generic;


import java.util.Arrays;

/*
* 1. 可变参数的工作原理
*
* java 编译器提供的语法糖，本质来说，就是将若干个参数组成一个数组，然后传递给对应的函数。
*
*
* */
public class GenericVarArgs {
    public static void main(String[] args) {
        /*
        * 这一步为什么是正确的？
        *
        * 工作流程如下(可以通过查看字节码确认)：
        * 1. 先创建一个 String[] 对象
        * 2. 再传递给 asArray(Object[])
        * 3. 然后将返回的 Object[] 转化为 String[]
        * 4. 因为 Object[] 指向对象的实际类型就是 String[]，所以转化是允许的。
        *
        * */
        String[] arr = asArray("one", "two", "three");
        System.out.println(Arrays.toString(arr));

        // :

        /*
        * 这个例子会报 ClassCastException，为什么？
        *
        * 由于类型擦除，在 pickTwo 中调用 asArray 的时候，会先创建 Object[] 而不是 String[]，
        * 因此 asArray() 的返回 Object[] 指向的对象的实际类型就是 Object[]，
        * 因此当转化为 String[] 的时候就会报 ClassCastException(即使 Object[] 中元素的实际类型是 String 也会报错)
        * */
        String[] firstTwo = pickTwo("one", "two", "three");
        System.out.println(Arrays.toString(firstTwo));
    }

    static <K> K[] pickTwo(K k1, K k2, K k3) {
        return asArray(k1, k2);
    }

    static <T> T[] asArray(T... objs) {
        return objs;
    }
}

