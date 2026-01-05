package org.example.util;

import javax.swing.*;

public class UtilTeacher extends UtilPerson{
    private String courseName;

    public UtilTeacher(String courseName, String teacherName) {
        super(teacherName);
        this.courseName = courseName;
    }


    @Override
    public String string() {
        return super.string() + "," + String.format("course: %d", courseName);
    }
}
