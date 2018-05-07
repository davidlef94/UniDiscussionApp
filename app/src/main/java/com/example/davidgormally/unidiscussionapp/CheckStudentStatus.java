package com.example.davidgormally.unidiscussionapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.davidgormally.unidiscussionapp.controller.StudentController;
import com.example.davidgormally.unidiscussionapp.model.student.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckStudentStatus extends AppCompatActivity {

    private Map<Boolean, String> onlineMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StudentController studentController = new StudentController(this);
        List<Student> students = studentController.getStudents();

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).isSignedIn()) {
                onlineMap.put(students.get(i).isSignedIn(), students.get(i).getId());
            }
        }

        if (!onlineMap.isEmpty()) {
            String id = onlineMap.get(true);
            Intent intent = HomeActivity.newIntent(this, id);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
    }
}
