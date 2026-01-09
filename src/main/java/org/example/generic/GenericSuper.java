package org.example.generic;

import jdk.jshell.execution.Util;
import org.example.util.UtilPerson;
import org.example.util.UtilStudent;

/*
* 描述 super 通配符(也称为下界通配符)相关内容
*
* 1. 解决什么问题
*
* 为了安全、复用地向 class 父类的泛型容器写入 class 及其子类对象。
*
* 2. 语法
*
* <? super class> 见下面例子。
*
* 3. 注意事项
*
* super 通配符，写操作是类型安全的，虽然我不知道具体的父类类型是什么，但是我知道一定是某个父类，所以赋值操作总是安全的。
* 但是，读操作是不允许的，因为我不知道具体的类型(当然，将读结果直接赋值给 Object 也是允许的)。
* */
public class GenericSuper {
    public static void main(String[] args) {
        Pair<UtilPerson> pair = new Pair<>();

        // 注意：这种赋值可行，是因为 Pair<? super UtilPerson> 和 Pair<UtilPerson> 是匹配的，并不是说明
        // Pair<UtilPerson> 继承自 Pair<? super UtilPerson>
        // 在 jvm 视角看，Pair<UtilPerson> 和 Pair<? super UtilPerson> 都是同一个类型：Pair
        Pair<? super UtilPerson> pairSuper = pair;
        UtilStudent stu1 = new UtilStudent(1, "xiao ming");
        UtilStudent stu2 = new UtilStudent(2, "xiao hong");

        copy(pairSuper, stu1, stu2);

        System.out.println(pairSuper.getFirst());
        System.out.println(pairSuper.getSecond());
    }

    private static void copy(Pair<? super UtilStudent> pair, UtilStudent stu1, UtilStudent stu2) {
       pair.setFirst(stu1);
       pair.setSecond(stu2);
    }
}
