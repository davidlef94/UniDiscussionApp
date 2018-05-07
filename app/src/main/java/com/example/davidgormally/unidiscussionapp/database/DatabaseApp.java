package com.example.davidgormally.unidiscussionapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.davidgormally.unidiscussionapp.model.colleague.Colleague;
import com.example.davidgormally.unidiscussionapp.model.colleague.ColleagueDao;
import com.example.davidgormally.unidiscussionapp.model.message.MessageContent;
import com.example.davidgormally.unidiscussionapp.model.message.MessageContentDao;
import com.example.davidgormally.unidiscussionapp.model.student.Student;
import com.example.davidgormally.unidiscussionapp.model.student.StudentDao;

@Database(entities = {Student.class, Colleague.class, MessageContent.class}, version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter.class)
public abstract class DatabaseApp extends RoomDatabase{

    private static DatabaseApp INSTANCE;
    public abstract StudentDao studentModel();
    public abstract ColleagueDao colleagueModel();
    public abstract MessageContentDao messageContentModel();

    public static DatabaseApp getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, DatabaseApp.class, "userStudentAndMessage.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }
}
