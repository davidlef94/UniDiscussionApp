package com.example.davidgormally.unidiscussionapp.model.message;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class MessageContent {

    @PrimaryKey
    @NonNull
    private String messageId;

    @ColumnInfo(name = "message_content")
    private String messageContent;

    @ColumnInfo(name = "student_message_belongs_to")
    private String studentMessageBelongsTo;

    @ColumnInfo(name = "date_received")
    private Date messageReceivedDate;


    public MessageContent() {

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getStudentMessageBelongsTo() {
        return studentMessageBelongsTo;
    }

    public void setStudentMessageBelongsTo(String studentMessageBelongsTo) {
        this.studentMessageBelongsTo = studentMessageBelongsTo;
    }

    public Date getMessageReceivedDate() {
        return messageReceivedDate;
    }

    public void setMessageReceivedDate(Date messageReceivedDate) {
        this.messageReceivedDate = messageReceivedDate;
    }

}
