package org.example.thread;

import java.util.concurrent.locks.StampedLock;

/*
* 本文描述 StampedLock
*
* 1. 解决什么问题
*
* StampedLock 也是读写锁，那跟 ReadWriteLock 的区别是什么呢？
*
* ReadWriteLock 中的读锁是悲观锁，这里的悲观指的是读的时候，会发生写操作。带来的问题是：读需要加锁，性能会差一点；如果有读操作，写操作需要等待。
*
* StampedLock 提供了读乐观锁，即乐观的认为读的时候不会发生写操作，底层并不会加锁，而是读取一个版本号，读取完数据后，需要再判断一下版本号是否
* 发生变化，如果没有发生变化，说明读期间没有写操作，读到的数据就是正确的，如果发生了变化，再可以重试，或者通过读悲观锁读取。
*
* 这样的好处是：读乐观锁无需加锁，性能更好；写操作可以立马加上锁。
*
* 2. 使用方法
*
* 见下面例子。
*
* 3. 注意事项
*
* 3.1 不可重入
* 3.2 除了 try 方法，其他方法不可中断。
*
* 因此，对于读多写极少，且可以接受不可重入、不可中断特性，就可以选择 StampedLock.
*
* */
public class MyStampedLock {
    private final StampedLock stampedLock = new StampedLock();
    private double value1;
    private double value2;

    private void readValue() {
        // 通过 tryOptimisticRead() 获取读乐观锁。
        // 返回值就是个版本号。
        long stamp = stampedLock.tryOptimisticRead();
        double value1 = this.value1;

        try{
            // 为了让写线程更改数据
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        double value2 = this.value2;

        // 说明读期间发生了写操作，我们这里降级为读悲观锁
        // 通过 validate 方法判断版本号是否发生变化
        if(!stampedLock.validate(stamp)) {
           System.out.println(Thread.currentThread().getName() + "读的时候，发生了写操作，降级为读悲观锁");

           // 通过 readLock() 方法获取读悲观锁
           stamp = stampedLock.readLock();

           value1 = this.value1;
           value2 = this.value2;

           // 通过 unlockRead() 释放悲观锁
           stampedLock.unlockRead(stamp);
        }

        System.out.println(String.format("value1: %f, value2: %f", value1, value2));
    }

    private void writeValue() {
        // 通过 writeLock() 获取写锁
        long stamp = stampedLock.writeLock();

        this.value1 += 1.0;
        this.value2 += 1.0;

        // 通过 unlockWrite() 释放读锁
        stampedLock.unlockWrite(stamp);
    }

    public static void main(String[] args) {
        MyStampedLock myStampedLock = new MyStampedLock();

        Thread readThread = new Thread(() -> {
            myStampedLock.readValue();
        });
        readThread.start();

        // 让读线程先执行
        try{
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }

        Thread writeThread = new Thread(() -> {
          myStampedLock.writeValue();
        });

        writeThread.start();
    }
}
