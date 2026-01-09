本文描述 java 中泛型相关的内容。

# 解决什么问题

解决重复代码的编写问题。

# 工作原理

java 的泛型可以理解为是 javac(java 编译器)提供的语法糖，javac 会擦除泛型相关的信息，将对应的 T 转化为上限类型，在
jvm 视角看，没有任何泛型相关的内容。

上限类型的确定逻辑为：
1. 如果泛型类型继承自某个类型，则转化为该类型。
2. 否则，转化为 Object。

# 注意事项

## 泛型的限制

### 类型参数 T 不能参与实例化

因为在 jvm 视角看，T 信息完全会丢失，所以 jvm 不知道实例化什么类型的实例。即使按照上限类型实例化，这就可能存在类型安全
的问题。

总的来说，如果运行 T 参与实例化，就无法保证类型安全了。

比如以下代码：
```java
public class Generic<T> {
    private T value;

    public Generic() {
        // 假设编译器运行如此实例化。
        value = new T();
    }
    
    public T getValue() {
        return value;
    }
} 

// 调用方
Generic<string> generic = new Generic<>();
// 这一步 getValue 返回的是 Object，转化为 String 会出现 ClassCastException
String str = generic.getValue();

```

### 不恰当的覆写方法

如果某个泛型方法经过擦除后覆写了父类的方法，编译器会阻止这种方法。原因如下：

- 语义不一致。这种覆写不是用户的本意。
- 即便允许，可能存在类型安全的问题。
```java
// 父类：返回值为String的非泛型方法
class Parent {
    public String getValue() {
        return "parent";
    }
}

// 子类：泛型方法，返回值为T
class Child extends Parent {
    public <T> T getValue() {
        return (T) new Integer(123); 
    }
}

// 多态调用：父类引用指向子类实例
Parent p = new Child();
String s = p.getValue(); // 运行时抛ClassCastException：Integer无法转为String

```

### 不可通过 new 实例化泛型数组

1. 含义

    对于泛型数组可以声明，但是不能通过 new 实例化。

2. 为什么
    
    因为泛型数组 + 数组协变可能会导致类型安全问题，因此 java 干脆禁止通过 new 来创建泛型数组。虽然可以通过反射来创建泛型数组，但是
    仍然存在类型安全的问题。
    
    考虑以下代码：
```java
public static void main(String[] args) {
   Pair<UtilStudent>[] students = new Pair<>[2];
   Object[] objects = students;
   // 编译器视角看 Object 存储 Pair 类型没有任何问题
    // jvm 视角看 objects 元素实际类型为 Pair(类型擦除)，因此赋值 Pair<UtilTeacher> 也不会导致 ArrayStoreException
   objects[0] = new Pair<UtilTeacher>("math", "lao wang");
    
    Pair<UtilStudent> pair = students[0];
    // 这里就会有类型安全问题，会触发 ClassCastException.
    UtilStudent first = pair.getFirst(); 
}
```
   
3. 如何实例化泛型数组

    要么使用 List<> 替换数组。如果非要用数组，只能读不能写，这样就能避免类型安全问题。

## java 泛型的不变性(Invariance)

### 含义

即使类 A 是类 B 的子类，基于它们的泛型参数化类型（比如 Pair<A>、Pair<B>）之间也不存在任何继承 / 子类型关系，
Pair<A> 既不是 Pair<B> 的子类，也不是 Pair<B> 的父类，二者无任何关联，这就是泛型的不变性。

### 解决什么问题

类型安全

### 通配符是否打破不变性

有了通配符(extends、super)虽然支持赋值操作，但这只是一种更灵活的匹配规则，并没有破坏**类型安全**。简单来说，这就是
javac 提供的一种语法糖，由 javac 保证类型安全，在 jvm 视角看，都是同一个类型。
