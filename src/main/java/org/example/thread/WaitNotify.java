package org.example.thread;

/*
* 描述 WaitNotify 相关内容。
*
* 1. 解决什么问题
*
* 在 synchronized 场景下，实现线程间的协同。其实跟条件变量基本一样。
*
* 2. 语法
*
* obj.wait(): 释放对象 monitor 锁，并阻塞。wait() 是在 Obj 中定义的 native 方法，由 jvm 实现具体的逻辑。
* obj.nitify(): 激活一个等待的线程。
* obj.nitifyAll(): 激活所有等待的线程。
*
* 3. 实现原理
*
* 本质与条件变量一致。
*
* 4. 注意事项
*
* 无。
*
* */
public class WaitNotify {
    private boolean flag;

    public synchronized void setFlag() {
       flag = true;
       System.out.println(String.format("[%s] set flag to ready", Thread.currentThread().getName()));
       this.notifyAll();
    }

    public synchronized void waitFlag() {
        while (!flag) {
            System.out.println(String.format("[%s] flag is not ready, wait", Thread.currentThread().getName()));
            try {
                this.wait();
            } catch (InterruptedException e) {
               System.out.println(e);
            }
        }

        System.out.println(String.format("[%s] flag is ready", Thread.currentThread().getName()));
    }

    public static void main(String[] args) {
        WaitNotify obj = new WaitNotify();

        Thread t1 = new Thread() {
            @Override
            public void run() {
                obj.waitFlag();
            }
        };

        t1.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println(e);
        }


      Thread t2 = new Thread() {
            @Override
            public void run() {
                obj.setFlag();
            }
      };

        t2.start();

        try {
            t1.join();
            t2.join();
        }  catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
