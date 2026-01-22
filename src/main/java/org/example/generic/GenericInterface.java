package org.example.generic;

public class GenericInterface {
    public static void main(String[] args) {
        MyGenericInterface<String> inter = new Impl();
        // 编译器在调用完 inter.run() 之后，会自动将 Object 转化为 string 类型。
        /*
        * 12: invokeinterface #16,  1           // InterfaceMethod org/example/generic/MyGenericInterface.run:()Ljava/lang/Object;
        * 将 Object 转化为 String
        * 17: checkcast     #22                 // class java/lang/String
        * 20: invokevirtual #24                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        * */
        System.out.println(inter.run());
    }
}


// 与泛型 class 类似，在 jvm 视角看，没有 T，只有 Object，javac 会自动加入类型转化指令。
interface MyGenericInterface<T> {
   // 该方法在字节码中声明如下：InterfaceMethod org/example/generic/MyGenericInterface.run:()Ljava/lang/Object;
   T run();
}

class Impl implements MyGenericInterface<String> {
    @Override
    public String run() {
        return "hello";
    }
}