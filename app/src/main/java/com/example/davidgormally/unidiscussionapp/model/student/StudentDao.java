package com.example.davidgormally.unidiscussionapp.model.student;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    void addStudent(Student student);

    @Update
    void updateStudent(Student student);

    @Query("select * from student where id =:id")
    Student getStudent(String id);

    @Query("select * from student")
    List<Student> getStudents();

}
