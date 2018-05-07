package com.example.davidgormally.unidiscussionapp.controller;

import android.content.Context;

import com.example.davidgormally.unidiscussionapp.database.DatabaseApp;
import com.example.davidgormally.unidiscussionapp.model.colleague.Colleague;

import java.util.List;

public class ColleagueController {

    private DatabaseApp databaseApp;

    public ColleagueController(Context context) {
        databaseApp = DatabaseApp.getInMemoryDatabase(context);
    }

    public void insertColleague(Colleague colleague) {
        databaseApp.colleagueModel().insertColleague(colleague);
    }

    public void deleteColleague(Colleague colleague) {
        databaseApp.colleagueModel().deleteColleague(colleague);
    }

    public void updateColleague(Colleague colleague) {
        databaseApp.colleagueModel().updateColleague(colleague);
    }

    public List<Colleague> getColleagues() {
        return databaseApp.colleagueModel().getColleagues();
    }

    public Colleague getColleague(String id) {
        return databaseApp.colleagueModel().getColleague(id);
    }

}
