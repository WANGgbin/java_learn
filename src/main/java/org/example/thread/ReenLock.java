package org.example.thread;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/*
* 介绍可重入锁相关的内容。
*
* 1. 解决什么问题
*
* 既然已经有了 synchronized，为什么还要提供 ReentrantLock ？
*
* synchronized 的优势就是简单，自动释放锁，大多数场景都能满足。但一些特殊功能 synchronized 无法满足。比如：
* - 公平锁: 避免饥饿，先阻塞的先获取到锁。synchronized 对应的锁是不公平的。
* - 中断线程阻塞: 如果线程正在等待锁，可以直接中断。
* - tryLock: 尝试获取。
* - 多条件变量: 基于同一个锁，创建多个条件变量。
*
* 上述高级特性，ReentrantLock 都可以满足。ReentrantLock 的问题在于需要我们自己手动释放锁，所以需要特别注意锁释放逻辑，避免没有正确
* 释放锁导致的死锁问题。
*
* 2. 使用方式
*
* 见下面例子。
*
* 3. 实现原理
*
* 4. 注意事项
*
* 要正确释放锁，避免死锁。
*
* */
public class ReenLock {
    private final ReentrantLock lock = new ReentrantLock();

    private boolean tryWork(long timeout) {
        try {
            // 通过 tryLock 方法尝试加锁，参数可以指定等待时间。
            if(lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 通过 unlock() 方法释放锁。
                    // 注意这里通过 finally 保证一定释放锁！！！
                    lock.unlock();
                }
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException e) {
           return false;
        }
    }

    public static void main(String[] args) {
       ReenLock obj = new ReenLock();
        ArrayList<Thread> threads = new ArrayList<>();

       for (int i = 0; i < 2; i++) {
           Thread thread = new Thread() {
               @Override
               public void run() {
                  if (obj.tryWork(10)) {
                      System.out.println(Thread.currentThread().getName() + " works");
                  } else {
                      System.out.println(Thread.currentThread().getName() + " does not work");
                  }
               }
           };

           threads.add(thread);
       }


       for (Thread thread: threads) {
           thread.start();
       }

       for (Thread thread: threads) {
           try {
               thread.join();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }
}
