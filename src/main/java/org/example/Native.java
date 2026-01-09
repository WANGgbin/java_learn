package org.example;

/*
 * 描述 native 特性。
 *
 * 在 java 中，可以用 native 来描述一个方法。这类方法与普通方法的区别在于：这类方法的逻辑不通过
 * java 实现，而是由 jvm 来实现。
 *
 * 在字节码层面的区别就是：native 描述的方法，没有 Code 属性，flags 中多了 ACC_NATIVE:
 *
 *   private static native void nativeMethod();
 *   descriptor: ()V
 *   flags: (0x010a) ACC_PRIVATE, ACC_STATIC, ACC_NATIVE
 *
 *  当 jvm 识别到一个方法是 native 方法后，就会去调用对应的本地函数(通过 C/C++ 实现)。
 *
 **/

public class Native {
    public static void main(String[] args) {
       nativeMethod();
    }

    private static native void nativeMethod();
}
