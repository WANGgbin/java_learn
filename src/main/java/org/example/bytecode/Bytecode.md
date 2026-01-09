本文描述 java 字节码相关内容。

# class 文件内容

java 源文件通过 javac 编译后会生成一个 class 文件。class 文件是人类不可读的，我们可以通过 `javap`
命令来查看 class 文件的内容。

一个 class 主要包含：

1. 常量池(静态常量池)
2. 每个方法的 bytecode、局部变量表

## 常量池

表示的是当前类涉及到的常量，比如 String、class 等。

在 class 文件中，常量池是通过分层引用的方式来表示的，目的在于减少 class 文件的大小。比如：

```text
// 常量索引  常量类别         常量值          javap 加入的注释
   #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
   #2 = Class              #4             // java/lang/Object
   #3 = NameAndType        #5:#6          // "<init>":()V
   #4 = Utf8               java/lang/Object // Utf8 就是字符串
   #5 = Utf8               <init>
   #6 = Utf8               ()V

```

我们先看下常量池的格式，#+数字 表示常量池的索引，bytecode 中会用。 = 之后的就是常量的类别，后面的部分就是常量的内容。

我们把 class 文件中的常量池也称为静态常量池。当 jvm 加载一个 class 的时候，会将 class 中的静态常量池转化为动态常量池。

动态常量池中的内容，可以简单理解为：#1 实际值/对象引用 的格式，比如对于 Class 类型常量，在 jvm 的动态常量池中，存放
的就是指向实际 Class 对象的引用。又比如，对于 String 类型常量，存放的就是指向 String 对象的引用。

需要注意的是，动态常量池也是类私有的，即每个类有自己的动态常量池，但是指向的常量则是全局共享的。

## 方法局部变量表

class 文件中，另一个很重要的部分就是每个方法的 bytecode，jvm 通过执行每一个 bytecode 来完成程序的执行。

每个方法有自己的栈帧，方法的入参、局部变量都在这个栈帧中，为了方便引用这些变量，定义了`局部变量表`的概念，比如：
```text
 LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      22     0  args   [Ljava/lang/String;
            3      19     1 varInt   I
            6      16     2 varString   Ljava/lang/String;
           17       5     3  util   Lorg/example/util/UtilBase;

```

Start、Length 表示变量的作用域，Slot 表示变量的槽位，也就是索引，Name 表示变量的名字，Signature 表示变量的类型。

其实，简单理解，就是局部变量以数组的形式存在方法函数栈帧中，通过索引 Slot 可以在 bytecode 中方便的引用局部变量。

## bytecode

我们看看 bytecode 是什么格式的。

```text
 0: bipush        6
 2: istore_1
 3: ldc           #7                  // String Hello
 5: astore_2
 6: new           #9                  // class org/example/annoymousclass/Closure$1

```

最前面的数字表示字节码偏移量，后面表示字节码指令，之后表示字节码指令的操作数。

# jvm 执行 bytecode 原理

执行 bytecode 无非就是从哪里取数据，进行什么样的操作，将计算结果写回到什么地方。

## 从哪里取数据

可以从动态常量池、方法局部变量、静态变量、栈顶 取数据。

## 什么样的操作

- store
- load
- ldc
- push
- ...

## 写回到什么地方

方法局部变量、栈顶。

# jvm 进程内容空间布局

## 线程私有

1. 线程虚拟栈帧
2. 线程 native 栈帧

## 线程共享

1. 堆
2. 方法区(元空间）
    方法区存放 java 类的元信息等。方法区是 java 规范中的概念。元空间是方法区的一种实现。

# 常见的 bytecode 指令

- ldc

全称 load constant 即加载常量。具体来说就是从类的运行时常量池读区数据并入栈。

比如 ldc #7 就是将常量池索引为 7 处的数据压入栈顶。


- iconst_1

加载常量 1 并入栈。 是加载常用 int 常量的快捷指令。

结构为：[类型前缀] + [常量标识] + [_常量值]

类型前缀：i
常量表示：const
常量值：1

如果要加载的常量比较大，jvm 会使用 ldc 指令。

- istore_1、aload_2

结构为：[类型前缀] + [操作动词] + [_数字]

类型前缀：
 - i: int(包含 byte/short/char/boolean) jvm 中这些类型都会提升为 int
 - l: long
 - d: double
 - f: float
 - a: reference，引用类型

操作动作：
 - store: 出栈栈顶的数据并存入到局部变量
 - load: 将局部变量入栈

数字：局部变量表的槽位。

- invoke*

  该系列的方法将函数返回值存储到栈顶。

  - invokevirutal
  
    一般是调用运行时才确定的方法。

  - invokespecial

    一般是调用编译时确定的方法。

- checkcast
    
   将当前栈顶的数据转化为 checkcast 参数指定的类型。 

- getfield、putfield
    
  - putfield
    
      putfield 执行前，必须先将两个操作数按固定顺序压入当前方法帧的操作数栈，指令本身还会携带一个「常量池索引参数」（用于定位要赋值的字段）
  
  - getfield

      其唯一的核心操作数（对象引用）从当前方法帧的操作数栈中获取；执行后，读取到的字段值会被压入当前方法帧的操作数栈顶（临时存储） 
   
- getstatic 

  用于读取类的「静态字段（类字段）」值，它不需要从操作数栈获取任何操作数（这是与 getfield 最核心的区别），仅通过指令自身携带的常量池索引定位静态字段；执行后，读取到的静态字段值会被压入当前方法帧的操作数栈顶（临时存储）

- putstatic

  用于给类的「静态字段（类字段）」赋值，其唯一的核心操作数（要赋值的字段值）从当前方法帧的操作数栈顶获取；指令自身携带常量池索引定位目标静态字段；执行后，字段值会被永久存储到方法区（元空间）的静态变量区，操作数栈弹出该值，无任何返回值压栈。

- aastore

  第一个 a 代表「引用类型（reference）」，astore 是「数组存储（Array Store）」的固定后缀；合起来 aastore 就是「向引用类型数组中存储元素」的指令

- dup

  dup 是 duplicate（复制）的缩写，作用是复制「操作数栈顶的元素」并将复制后的元素重新压回栈顶