package org.example.thread;

/*
* 本文描述 synchronized 相关内容。
*
* 1. 解决什么问题
*
* 并发安全问题，对于某个方法、或者代码块，如果存在并发访问，就可以通过 synchronized 修饰符保证并发安全。
*
* 2. 语法
*
* 可以修饰方法，也可以修饰代码块，详情见下面的例子。
*
* 每个 synchronized 都会关联一个对象，对于代码块，需要我们手动指定关联的对象。对于静态方法，对应的对象就是 Class.class 对象，
* 对于非静态方法，对应的对象就是当前对象 this。对于 synchronized 方法，对应的对象无需用户手动指定。
*
* 3. 实现原理
*
* java 中每个对象内部都有个 monitor 锁(可重入锁)，synchronized 方法/代码块，会先对该对象加锁，在执行完毕后释放锁，保证了并发安全。
*
* 对于 synchronized 方法编译为字节码后，会多一个 ACC_SYNCHRONIZED 标记，jvm 会识别该标记，并进行加锁/释放锁操作。
*
* 对于 synchronized 代码块，编译的字节码，对应两条指令：monitorenter(加对象的 monitor 锁)、monitorexit(释放对象的 monitor 锁)。
*
* 这里还涉及一个锁升级的过程，因为如果锁已经被占用，当一个线程再次占有锁的时候，会陷入内核并阻塞，这涉及到用户态与内核态转化，涉及到线程的阻塞、切换，效率较低。
* 所以，思路是先通过 CAS 的方式尝试加锁，如果尝试几次后还没有加到锁，再升级为重量锁。
*
* 4. 注意事项
*
* 无。
*
* */

public class Synchronized {
    private volatile static int count = 0;

    // unsafeIncrCount 非并发安全
    private static void unsafeIncrCount() {
       for (int i = 0; i < 10000; i++)  {
           count += 1;
       }
    }

    // safeIncrCount 并发安全
    private static synchronized void safeIncrCount() {
        for (int i = 0; i < 10000; i++)  {
            count += 1;
        }
    }

    private static void safeIncrCountCodeBlock() {
        synchronized(Synchronized.class) {
            for (int i = 0; i < 10000; i++)  {
                count += 1;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                unsafeIncrCount();
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                unsafeIncrCount();
            }
        };

        t1.start();
        t2.start();

        t1.join();
        t2.join();

       /*
       * unsafeIncrCount() 不是并发安全的，这里输出的 count 也不等于 20000
       * */
       System.out.println(count);


       count = 0;

        Thread t3 = new Thread() {
            @Override
            public void run() {
                safeIncrCount();
            }
        };

        Thread t4 = new Thread() {
            @Override
            public void run() {
                safeIncrCount();
            }
        };

        t3.start();
        t4.start();

        t3.join();
        t4.join();

        /*
         * saefIncrCount() 是并发安全的，这里输出的 count 等于 20000
         * */
        System.out.println(count);

    }
}
