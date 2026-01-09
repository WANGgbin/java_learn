package org.example.util;

public class UtilStudent extends UtilPerson{
    private int grade;

    public UtilStudent(int grade, String name) {
        super(name);
        this.grade = grade;
    }

    public int getGrade() {
        return this.grade;
    }

    @Override
    public String string() {
       return super.string() + ", " + String.format("grade: %d", grade);
    }
}
