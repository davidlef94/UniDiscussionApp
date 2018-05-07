package com.example.davidgormally.unidiscussionapp.controller;

import android.content.Context;

import com.example.davidgormally.unidiscussionapp.database.DatabaseApp;
import com.example.davidgormally.unidiscussionapp.model.message.MessageContent;

import java.util.List;

public class MessageController {

    private DatabaseApp databaseApp;

    public MessageController(Context context) {
        databaseApp = DatabaseApp.getInMemoryDatabase(context);
    }

    public void insertMessage(MessageContent messageContent) {
        databaseApp.messageContentModel().insertMessageContent(messageContent);
    }

    public void deleteMessage(MessageContent messageContent) {
        databaseApp.messageContentModel().deleteMessageContent(messageContent);
    }

    public List<MessageContent> getAllMessages() {
        return databaseApp.messageContentModel().getMessages();
    }

    public List<MessageContent> getMessages(String studentId) {
        return databaseApp.messageContentModel().getMessageContent(studentId);
    }
}
