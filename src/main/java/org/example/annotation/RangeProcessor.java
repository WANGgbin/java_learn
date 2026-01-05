package org.example.annotation;

import java.lang.reflect.Field;

public class RangeProcessor {
    public static void process(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getFields();

        for (Field field: fields) {
            Range range = field.getAnnotation(Range.class);
            if (range == null) {
               continue;
            }

            Object value = field.get(obj);
            if (value instanceof String str) {
                if (str.length() < range.min() || str.length() > range.max())  {
                    throw new IllegalArgumentException(String.format("length of field: %s is illegal", field.getName()));
                }
            }
        }
    }
}


