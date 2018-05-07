package com.example.davidgormally.unidiscussionapp.model.message;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MessageContentDao {

    @Insert
    void insertMessageContent(MessageContent messageContent);

    @Delete
    void deleteMessageContent(MessageContent messageContent);

    @Query("select * from messagecontent")
    List<MessageContent> getMessages();

    @Query("select * from messagecontent where student_message_belongs_to =:id")
    List<MessageContent> getMessageContent(String id);

}
