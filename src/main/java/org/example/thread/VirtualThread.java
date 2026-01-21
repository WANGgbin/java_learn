package org.example.thread;

/*
*
* 1. 解决什么问题
*
* 其实跟 golang 中的 goroutine 差不多。本质是一种用户态线程，更加轻量，上下文更少，切换也只需要在用户态进行。相比于 os 线程，
* 也可以创建更多的用户态协程。
*
* 尤其在 i/o 密集型场景下，协程的优势更加明显。
*
* 该特性是在 java21 正式推出的。
*
* 2. 使用方式
*
* 3. 实现原理
*
* 与 goroutine 差不多，也在用户态实现了一个调度机制。
*
* 4. 注意事项
*
* */
public class VirtualThread {
}
