package com.example.davidgormally.unidiscussionapp.controller;


import android.content.Context;

import com.example.davidgormally.unidiscussionapp.database.DatabaseApp;
import com.example.davidgormally.unidiscussionapp.model.student.Student;

import java.util.List;

public class StudentController {

    private DatabaseApp databaseApp;

    public StudentController(Context context) {
        databaseApp = DatabaseApp.getInMemoryDatabase(context);
    }

    public void addStudent(Student student) {
        databaseApp.studentModel().addStudent(student);
    }

    public void updateStudent(Student student) {
        databaseApp.studentModel().updateStudent(student);
    }

    public Student getStudent(String id) {
        return databaseApp.studentModel().getStudent(id);
    }

    public List<Student> getStudents() {
        return databaseApp.studentModel().getStudents();
    }
}
