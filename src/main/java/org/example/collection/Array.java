package org.example.collection;

import org.example.util.UtilPerson;
import org.example.util.UtilStudent;
import org.example.util.UtilTeacher;

/*
* 介绍 java Array 内容。
*
* 1. 数组协变特性(Covariance)
*
* 1.1 含义
*
* 这里的协变指的是：如果类 A 是类 B 的子类，那么数组类型 A [] 也会是 B [] 的子类（比如 Integer[] 是 Number[] 的子类）。
*
* 1.2 使用场景
*
* 如果一个方法，想处理不同类型的数组，就可以使用数组协变特性，避免重复写多个函数。比如 someMethod(Number[] nums) 既可以接受
* Integer[] 又可以处理 Double[]。
*
* 1.3 注意事项
*
* 1.3.1 类型安全问题
*
* 数组协变会引入类型安全问题，可能导致运行时 ArrayStoreException 错误。类型安全问题，编译器无法前置判断，只能延迟到运行时。
* 详情见下面的例子。
*
* 因此对于数组协变的一个最佳实践是：**只读不写**。
*
* 1.3.2 不能向下协变
*
* 数组的协变，只能是向上的协变，向下会编译错误。
* */
public class Array {

    public static void main(String[] args) {
       UtilStudent[] studentList = new UtilStudent[]{new UtilStudent(1, "xiao ming")};
       // 这里就是 array 的类型协变，UtilStudent[] 是 UtilPerson[] 的子类。
       handlePerson(studentList);

        // 类型不安全的例子
        // 运行时报错：ArrayStoreException
       unsafe(studentList);

       // 不能向下协变，即使实际存储的元素类型就是 String，编译错误。
//        String[] arr = new Object[]{new String("abc")};
    }

    private static void handlePerson(UtilPerson[] personList) {
        for (UtilPerson person: personList) {
            System.out.println(person.string());
        }
    }

    private static void unsafe(UtilPerson[] personList) {
        if (personList.length == 0) {return;}

        /*
        * 在编译器视角看，将 UtilTeacher 赋值给 UtilPerson，这没有任何问题，
        * 但是 UtilPerson[] 指向对象的实际数组类型是什么编译器不知道，只能交给运行时，
        * 如果赋值变量与底层类型不一致，就会导致 ArrayStoreException
        *
        * 数组对象的「实际类型」指的是数组本身的类型（如 String[]、Object[]），而非数组中元素的类型（如 String）
        * */
        personList[0] = new UtilTeacher("math", "lao wang");
    }
}
