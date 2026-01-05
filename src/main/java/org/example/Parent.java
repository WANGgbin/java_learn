package org.example;

import org.example.annotation.Range;

public class Parent {
    @Range(min = 1, max = 10)
    private String name;

    public Parent(String name) {
       this.name = name;
    }

    public String getName() {
        return name;
    }
}
