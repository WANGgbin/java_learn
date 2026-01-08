package org.example.thread;

import java.util.concurrent.Semaphore;

/*
* 本文描述 java semaphore 相关内容。
*
* 1. 解决什么问题
*
* 有一类资源，允许同时有多个线程访问，如果使用 ReentrantLock，就需要创建多个锁，比较麻烦。
*
* 2. 如何使用
*
* 见下面例子。
*
* 3. 底层原理
*
* 4. 注意事项
*
* 避免没有正确释放信号量
*
* */
public class MySemaphore {
    private static final Semaphore semaphore = new Semaphore(2);

    public static void main(String[] args)  {
        for(int i = 0; i < 10; i++) {
           Thread thread = new Thread(){
               @Override
               public void run() {
                   try{
                       // 通过调用 acquire 方法，占用信号量
                       semaphore.acquire();
                       try{
                           System.out.println(Thread.currentThread().getName() + " is working...");
                           Thread.sleep(1000);
                       } finally {
                           // 通过调用 release() 释放信号量
                            semaphore.release();
                       }
                   } catch (InterruptedException e) {
                       // 重新设置线程的中断标记，这样其他地方可以继续处理这个中断信号
                        Thread.currentThread().interrupt();
                   }
               }
           };

           thread.start();
        }
    }
}
