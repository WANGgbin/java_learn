package org.example.generic;

import org.example.util.UtilPerson;

public class Generic<T> extends UtilPerson {
   private T value;

   public T getValue() {
       return value;
   }

   public void setValue(T value) {
       this.value = value;
   }

   public static void main(String[] args) {
       Generic<Integer> generic = new Generic<>();

       generic.setValue(new Integer(1));

       // javac 会自动在这里加入类型转化，将 object 转化为 Integer
       Integer value = generic.getValue();
       System.out.println(value);
   }
}
