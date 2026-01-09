package org.example.thread;

/*
* 描述 volatile 相关内容。
*
* 1. 解决什么问题
*
* 解决变量可见性的问题以及防止指令重排的问题。
*
* 1.1 可见性问题
*
* 我们知道每个 cpu 核有自己的 cache、store buffer。为了性能考虑，如果变量没有加载到 cache line，
* cpu 会将更新后的变量值先写入到 store buffer，之后在某个时机再更新到 cache line 中。
*
* 这带来的问题就是全局可见性的问题，对于其他 cpu 核而言，还是看不到最新的值。由全局可见性会引入所谓的乱序问题。
*
* 解决此问题的方式就是内存屏障，保证在执行内存屏障之后的命令时，保证之前的变量能更新到主存中，这样其他的 cpu 核就能及时
* 看到变量更新后的值。
*
* 1.2 指令重排
*
* 编译器或者 cpu 为了优化可能会调整指令的执行。站在单线程的视角看，这些指令重排不会有任何问题。但在多线程场景下，就可能会有问题。
*
* 一个典型的例子是 java 的单例模式，实例化的正常流程是：1、分配内存 2、实例化对象 3、将对象赋值给 Singleton。但是这几步之间可能
* 会发生指令重排，导致先将对象赋值给 Singleton 在实例化对象。这样其他线程拿到的 Singleton 可能就是未完整初始化的对象，后续访问
* 就可能出现 NPE(空指针异常)。
*
* 这里有个问题，jvm 不是按照字节码一条条执行指令的吗，理论上不可能出现乱序呢？是的，字节码是这样的。但是，对于一些热点代码，jvm 引入了
* jit 机制，会将字节码直接编译为 cpu 指令，然后直接执行 cpu 指令，这里不管是编译还是执行就可能出现乱序，volatile 解决的乱序问题就是
* 这里的乱序问题。
*
* 乱序执行的原因有两种：要么就是真的乱序执行指令了；要么是由于可见性问题导致其他线程误以为某个线程乱序执行了。
*
* 2. 用法
*
* 通过 volatile 关键字修饰变量。
*
* 3. 实现原理
*
* volatile 底层通过内存屏障实现。
*
* 内存屏障根据类别可以分为:
* - Load-Load: 保证内存屏障指令前后的读指令的顺序执行，即只有屏障之前的读指令执行完毕，才会执行屏障之后的读指令。
* - Load-Store: 保证内存屏障指令前的读指令与屏障后的写指令的顺序执行。即只有屏障之前的读指令执行完毕，才会执行屏障之后的写指令。
* - Store-Load: 保证内存屏障指令前的写指令与屏障后的读指令的顺序执行。即只有屏障之前的写指令执行完毕，才会执行屏障之后的读指令。另外一点就是
* 保证屏障之前写指令全局可见后，才会执行屏障后的指令。
* - Store-Store: 保证内存屏障指令前后的写指令顺序执行。此外，也会保证屏障之前的写操作全局可见后，才执行屏障之后的写指令。
*
* 注意上述的内存屏障分类是规范，不同的 cpu 架构有不同的实现，有些乱序场景可能就不存在，屏障指令也不相同。比如对于 X86 架构来说，只存在 Store-Load 乱序的问题，其他几种乱序场景压根就不存在。
*
* 对于 volatile 修饰的变量，读写操作下加的屏障如下：
* - 写操作
*   写操作，我们要保证之前的读写指令一定在写操作之前完成，之后的读写指令一定在写操作之后完成。所以在写操作前后都会加入屏障。
*   写之前加入 StoreStore、LoadStore 屏障，写之后加入 StoreStore、StoreLoad 屏障。
* - 读操作
*   似乎只需要保证读指令之后读写指令一定发生在读操作之后，只需要在读操作之后加入屏障即可。
*   读之后加入 LoadStore、LoadLoad 屏障。
*
* 4. 注意事项
*
* volatile 只能用来修饰类的变量(成员/静态)，可以修饰基本类型、引用类型。不能修饰 final、参数、局部变量。
*
* volatile 并不解决原子的问题。
*
* */
public class Volatile {

    private static volatile String name;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (name == null) {
                    System.out.println(name + " is not ready");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
                System.out.println(name + " is ready");
            }
        };

        thread.start();

        Thread.sleep(100);

        name = "ready";

        thread.join();
    }
}
