package com.example.davidgormally.unidiscussionapp.model.colleague;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ColleagueDao {

    @Insert
    void insertColleague(Colleague colleague);

    @Delete
    void deleteColleague(Colleague colleague);

    @Update
    void updateColleague(Colleague colleague);

    @Query("select * from colleague")
    List<Colleague> getColleagues();

    @Query("select * from colleague where id =:colleagueId")
    Colleague getColleague(String colleagueId);

}
