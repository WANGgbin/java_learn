package org.example.thread;

/*
*
* 描述 java 中 thread local 相关内容。
*
* 1. 解决什么问题
*
* 1.1 方便传递线程维度的数据
*
* 这实际上与 go 中的 context 有点类似，请求维度的数据就可以通过 thread local 传递。
*
*
* 2. 使用方式
*
* 见下面例子。
*
* 3. 实现原理
*
* ThreadLocal 内部首先会获取当前线程，然后操作当前线程的 ThreadLocalMap:
*
* 3.1 set()
*
* ThreadLocalMap 的初始化时延迟初始化的，这个引用刚开始值为 null，当首次 set/get 的时候，才会
* 初始化这个 map。
*
* 当执行 ThreadLocal 对象的 set() 时候，底层就是在 ThreadLocalMap 中写入一个 Entry，其中：
* key 时 ThreadLocal对象 的弱引用(WeakReference)，value 是 set() 参数指定的对象的强引用。
*
*
* 3.2 get()
*
* get() 特殊点在于，如果对应的 Key 不存在，则会返回 Key 对应 ThreadLocal 对象的默认值，并将这个 Key、Value
* 组合写入到 ThreadLocalMap 中。
*
* 4. 注意事项
*
* 4.1 为什么 Key 是弱引用
*
* 还是站在内存泄漏角度考虑的，这样不会出现因为 ThreadLocalMap 中存在 ThreadLocal 强引用导致的 ThreadLocal 对象的泄漏问题。
*
* 4.2 为什么要调用 remove 方法
*
* 因为 Value 是强引用，而且线程经常是被复用的，只要 Thread 对象存在，那么 ThreadLocalMap 就存在，那指向 Value 的
* 强引用就会一致存在，这会导致两个问题：
* - 内存泄漏，因为 Value 存在强引用，所以对象无法释放。
* - 错误的值，Value 为旧值，可能不是我们希望的值。
*
* 4.3 与 TLS 的区别
*
* 注意，java 中的 ThreadLocal 与 os 线程的 TLS(Thread Local Storage) 没有任何关系。
*
* java 中可以通过 currentThread() 获取当前线程，该方法是个 native 方法，会从 TLS 中获取当前 Thread 对象的引用，
* jvm 会在线程的 TLS 中存入当前线程 Thread 对象的引用。
*
* */
public class MyThreadLocal {
    // 先声明一个 ThreadLocal 类型变量
    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                // set() 内部加入一个 Key、Value 到线程的 ThreadLocalMap 中
                token.set("token1");
                System.out.println(Thread.currentThread().getName() + "'s token is " + token.get());
                // 使用完毕后，一定要记得 remove()
                token.remove();
            }
        };
        Thread t2 = new Thread(){
            @Override
            public void run() {
                token.set("token2");
                System.out.println(Thread.currentThread().getName() + "'s token is " + token.get());
                token.remove();
            }
        };

        t1.start();
        t2.start();
    }
}
