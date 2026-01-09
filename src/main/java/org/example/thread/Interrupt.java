package org.example.thread;

import java.lang.Thread;

/*
* 描述 java 线程中断相关的内容。
*
* 1. 解决什么问题
*
* 请求终止线程。
*
* 为什么不直接终止线程呢？主要是直接终止线程，可能会导致一些资源无法释放(文件描述符、锁等)。而 interrupt() 是提供一种线程
* 优雅退出的方式。不直接杀掉线程，而是告诉线程你该退出了，线程收到这个信号后，就可以做一些资源释放的操作，然后停止运行，当然也可以
* 选择忽略该中断请求。
*
* 2. 使用方式
*
* 见下面的例子。
*
* 3. 底层原理
*
* 每个线程都有一个 interrupt 标记位，thread.interrupt() 方法仅仅是设置这个标记，来告诉线程你应该停止运行了。
* 至于是否真的停止运行，由线程自己来决定。
*
* 3.1 interrupt()
*
* 在线程运行态和阻塞态下调用 interrupt()，具体逻辑还不太一样。
*
* 运行态：设置中断标记。被中断的线程，需要主动调用 isInterrupted() 方法来检查该标记。
*
* 阻塞态：设置中断标记，激活阻塞的线程，抛出一个 InterruptedException 的异常，并清空中断标记。这里如果不清空标记，后序线程在调用
* 可中断阻塞方法时，发现中断标记被设置，会再次抛出异常。
*
* 3.2 isInterrupted()
*
* 就是用来检查 interrupt 标记位。
*
* 4. 注意事项
*
* */
public class Interrupt {
    public static void main(String[] args) throws InterruptedException {
        // 模拟线程阻塞的时候收到中断请求
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println(String.format("%s 阻塞.", Thread.currentThread().getName()));
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // 阻塞状态下被中断后，会抛出该异常
                    // 清理一些资源然后退出线程
                    System.out.println(String.format("%s 被中断，执行清理动作并推出", Thread.currentThread().getName()));
                }
            }
        };

        t.start();
        Thread.sleep(1000);

        // 调用此方法中断线程
        t.interrupt();
        t.join();

        // 模拟线程运行时候收到中断请求
        Thread t1 = new Thread() {
            @Override
            public void run() {
                // 循环中判断当前线程是否被终止。
                // 运行态的线程需要主动调用 isInterrupted() 来判断是否接收到退出信号
                while(!Thread.currentThread().isInterrupted()) {
                   System.out.println(currentThread().getName() + " is working...");
                }

                // 执行一些资源释放操作
                System.out.println(currentThread().getName() + " 接收到退出信息，释放资源并退出");
            }
        };

        t1.start();
        Thread.sleep(1);

        // 调用此方法中断线程
        t1.interrupt();
        t.join();

    }
}