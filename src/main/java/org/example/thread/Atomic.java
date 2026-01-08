package org.example.thread;

import java.util.concurrent.atomic.AtomicLong;

/*
* 本文描述 Atomic 相关内容。
*
* Atomic 是另一种保证并发安全的方式，相对于锁而言性能更好。
*
* 一些类型的读写(int、boolean、引用)本身就是原子的，不需要 Atomic。但是一些涉及到多个操作的逻辑，就需要 Atomic 机制。比如 CAS(CompareAndSwap)，
* 需要先读取变量，判断是否相等，再进行交换。
*
* java Atomic 的实现，本质就是一些 native 方法，由 jvm 实现。
*
* */
public class Atomic {
    public static void main(String[] args) {
        AtomicLong atomicLong = new AtomicLong();
        System.out.println(atomicLong.incrementAndGet());
    }
}
