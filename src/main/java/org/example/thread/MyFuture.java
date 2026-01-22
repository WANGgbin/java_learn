package org.example.thread;

import java.util.concurrent.*;

/*
* 1. 解决什么问题
*
* 获取异步执行结果的机制。
*
* */
public class MyFuture {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> future = executor.submit(new MyCall());

        // get() 方法同步阻塞等待结果。
        System.out.println(future.get());
    }
}

class MyCall implements Callable<String> {
    public String call() {
        return "hello";
    }
}
