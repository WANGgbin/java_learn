package org.example.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
* 描述线程池相关内容。
*
* 1. 解决什么问题
*
* 线程的创建和销毁是有代价的，所以通过线程池的方式复用线程。
*
* 2. 使用方式
*
* 见下面的例子。
*
* 3. 实现原理
*
*
* 4. 注意事项
*
* 4.1 能提交多少个任务
*
* 在创建线程池的时候可以指定任务队列，如果是有界的，则能提交的任务数量是有限的，超过队列大小的时候，就会执行拒绝策略，默认是抛出异常。
* 如果队列是无界的，则提交任务数量取决于 jvm 内存。
*
* 4.2 线程池中线程的数量如何决定
*
* 参考：https://www.yuque.com/hollis666/vhr2ge/zanzx4giay7gixf6
*
* 核心来说就是，对于 cpu 密集型，通常线程数与 cpu 核数相同，对于 io 密集型，设置 cpu 核数 * (1 + 线程等待时间/计算时间) 的线程数，但是
* 实际数量还需要根据实际压测结果调整。
*
* */
public class MyThreadPool {

    public static void fixedThreadPool() {
        // newFixedThreadPool 创建固定数量的线程
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i =  0; i < 10; i++) {
            executor.submit(new Task(String.format("task-%d", i)));
        }

        // shutdown 只是告诉 threadPool 不再接受新的任务，并不会等待所有已提交的任务执行完毕。
        // 可以调用 awaitTermination() 等待线程池关闭。

        // executor.shutdown();

        // shutdownNow() 执行后，不再执行新的任务，同时会尝试终止正在执行的任务，注意这里是尝试，并不保证一定会终止，
        // 因为通常是通过 thread.interrupt() 来实现终止，如果执行的任务中忽略了中断，则无法中断任务。
        // 同样可以调用 awaitTermination() 等待线程池关闭。

        executor.shutdownNow();

        // 这里线程池并没有结束
        if (!executor.isTerminated()) {
            System.out.println("thread pool is not terminated");
            try {
                // 等待 10s 后线程池真正结束
                if(executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.out.println("thread pool is terminated");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 定时任务线程池
    public static void scheduledThreadPool() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

        // AtFixedRate 表示以固定的速率执行任务，这里需要注意的是，如果任务执行时间超过了调度时间，任务并不会并行执行，
        // 而是会将任务加入队列，这样在当前任务执行完毕后，会立即执行下一个任务。
        executor.scheduleAtFixedRate(new Task(String.format("task-%d", 0)), 1, 1, TimeUnit.SECONDS);

        // WithFixedDelay 在任务执行完毕 + 固定延迟后，才会执行下一个任务。
        // executor.scheduleWithFixedDelay(new Task(String.format("task-%d", 0)), 1, 1, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        scheduledThreadPool();
    }
}

class Task implements Runnable {
    private final String name;
    private int count;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        count += 1;
       System.out.println(String.format("%s-%d starts at %s", name, count, System.currentTimeMillis()/1000));

       // 通过 for 循环，模拟忽略中断信号，继续执行
       for (int i = 0; i < 4; i++) {
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               // 在调用 shutdownNow() 时，会抛出中断异常。
                e.printStackTrace();
           }
       }

       System.out.println(String.format("%s-%d ends at %s", name, count, System.currentTimeMillis()/1000));
    }
}