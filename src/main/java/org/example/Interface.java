package org.example;

/*
* 描述 java 中 interface 相关内容。
*
* java 中的 interface 是不能实例化的，当我们给一个 interface 类型的引用赋值的时候，
* 实际上并不会实例化一个 interface 类型的实例，引用还是指向实际类型的实例。
*
* jvm 中通过一个 InterfaceKlass 对象来描述一个 interface，该对象包含了接口定义的方法、常量等。
*
* 对于普通的 class，jvm 通过一个 InstanceKlass 来描述，当该 class 实现了某些接口的时候，InstanceKlass 通过
* _implemented_interfaces 指向 InterfaceKlass。
*
* 通过接口调用方法，javac 对应的字节码指令是 invokeinterface，该命令的工作流程为：
* 1. 从栈顶引用出发找到对应的实例。
* 2. 从实例中找到实例对应 class 的 InstanceKlass
* 3. 判断是否实现对应的接口
* 4. 如果实现则从 InstanceKlass 的方法表中找到对应的方法
* 5. 执行调用
*
* invokeinterface 指令有两个参数，第一个指定哪个接口的哪个方法，第二个指定该方法的参数个数(this 也包含在内)
* */

public class Interface {
}
