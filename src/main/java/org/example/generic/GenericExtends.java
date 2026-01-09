package org.example.generic;

import org.example.util.UtilPerson;
import org.example.util.UtilStudent;

// GenericExtends 描述 extends 通配符(上界通配符) 相关内容
/*
* 1. 解决什么问题
*
* 如果一个方法想处理泛型类型为某一个类所有子类的参数，就可以考虑使用 extends 通配符。这样可以解决代码重复的问题，没有必要
* 为每一个子类型都定义一个函数。
*
* 2. 语法
*
*  <? extends BaseClass>，参考下面的例子。
*
* 注意：Pair<UtilStudent>、Pair<UtilTeacher> 都是 Pair<? extends UtilPerson> 的子类，是的，是子类关系。
* 但是，Pair<UtilStudent> 却不是 Pair<UtilPerson> 的子类，两者没有任何关系，原因是为了类型安全考虑。
*
* <?> 是 <? extends Object> 的简写，也称为无界通配符。
* 当一个方法对于接受的泛型类型没有任何限制的时候，就可以考虑使用 <?>。

* 3. 注意事项
*
* extends 通配符申明的变量，是不支持更改的。这也与 ? 表达的含义一致，我只知道是某个类的子类，但是我不知道具体的类型，那就
* 更谈不上修改了。
*
* 4. 与<T extends BaseClass> 的区别？
*
* 如果是为了支持泛型参数为某一个类以及子类，我们也完全可以使用 <T extends BaseClass> 对类型进行约束。两者的区别是什么呢？
* <? extends BaseClass> 表达明确的只读语义。但是 <T extends BaseClass> 是支持写操作的。
*
* 5. 通配符
*
* 不管是上界通配符、下界通配符、无界通配符，本质都是在解决两个问题：1. 类型安全 2. 代码复用。
* 通过这些语法，编译器能帮助我们进行类型安全检查。相比于直接操作 Object，更安全。
*
* */
public class GenericExtends {
    public static void main(String[] args) {
        Pair<UtilStudent> studentPair = new Pair<UtilStudent>(new UtilStudent(1, "one"), new UtilStudent(2, "two"));
        handlePair(studentPair);
    }

    private static void handlePair(Pair<? extends UtilPerson> pair) {
//  这里编译器会报错。即使我们能明确赋值不会导致类型安全问题。
//        if (pair.getFirst() instanceof  UtilStudent firstStudent) {
//           pair.setFirst(new UtilStudent(1, "three"));
//        }
        System.out.println(pair.getFirst());
        System.out.println(pair.getSecond());
    }
}


