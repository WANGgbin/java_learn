package org.example.util;

public class UtilPerson {
    private final String name;

    public UtilPerson() {
        name = "";
    }

    public UtilPerson(String name) {
        this.name = name;
    }

    public void doWork() {
        System.out.println(String.format("[%s] %s works", UtilPerson.class.getName(), this.name));
    }

    public void genericOverride(Object obj) {
        System.out.println(UtilPerson.class.getName() + "" + obj);
    }

    public String string() {
        return String.format("name: %s", name);
    }
}
