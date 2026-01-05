本文介绍 java 中闭包相关的概念。

# 解决什么问题

有了闭包，匿名类或者 lambda 便可以访问外部的数据，包括：外部类的实例数据、静态数据或者外部方法的局部变量。

# 原理

当匿名类或者 lambda(后面都以匿名类举例)捕获了外部变量，实际会在匿名类内部创建一个变量，该变量的值等于被
捕获变量的值(值类型就是相等，引用类型就是相同引用)。

同时在匿名类的构造函数中，会额外加入参数，在实例初始化的时候，将捕获变量的值赋给值内部变量。

以上原理，我们可以通过查看 javac 生成的 class 文件验证。 比如对于 Closure.java 编译后也会对匿名类生成一个 class 文件，
匿名类的命名格式为 外部类名$数字，对应的 class 文件反编译后内容如下：
```java
class Closure$1 extends UtilBase {
    // $FF: synthetic field
    final String val$varString; // 生成的字段，类型为 final
    
    // 构造函数中会加入额外的参数
    Closure$1(String var1, String var2) {
        super(var1);
        this.val$varString = var2;
    }

    public void doWork() {
        System.out.println(String.format("[%s] %s works", this.getClass().getName(), this.val$varString));
    }
}
```

我们再看看 Closure.class 的字节码：

```text
 0: bipush        6
 2: istore_1
 3: ldc           #7                  // String Hello
 5: astore_2
 // new 创建一个 Closure$1 对象
 6: new           #9                  // class org/example/annoymousclass/Closure$1
 9: dup
 // ldc 加载 base 常量入栈
10: ldc           #11                 // String base
// 将 slot2 局部变量入栈，也就是 Hello 入栈
12: aload_2
// 调用 Closure$1 的构造函数初始化 new 创建的对象
13: invokespecial #13                 // Method org/example/annoymousclass/Closure$1."<init>":(Ljava/lang/String;Ljava/lang/String;)V
// 将对象存到局部变量 slot3
16: astore_3

```

# 注意事项

对于外部方法的局部变量，只能捕获 final 或者 effective final(未显示申明为 final，但是只初始化一次) 的变量。主要是从语义一致性考虑，如果被捕获的变量
后续发生了变化，闭包中捕获的值跟外部变量的实际值不相同，会有歧义。