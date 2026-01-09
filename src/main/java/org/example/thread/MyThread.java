package org.example.thread;

/*
 * 介绍线程相关的内容。
 *
 *
 * **/
public class MyThread {
    public static void main(String[] args) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };

        // 调用 start() 方法启动一个新的线程
        thread.start();

        // 等待线程执行完毕后继续执行。
        try {
            thread.join();
        } catch (InterruptedException e) {
           System.out.println(e);
        }
    }
}
