package com.example.davidgormally.unidiscussionapp.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.davidgormally.unidiscussionapp.DiscussionFragment;
import com.example.davidgormally.unidiscussionapp.controller.MessageController;
import com.example.davidgormally.unidiscussionapp.model.message.MessageContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

public class SendMessageTask implements Runnable {

    private static final String TAG = "SendMessageTask";

    private Context context;
    private DiscussionFragment discussionFragment;
    private String studentId;

    public SendMessageTask(Context context, DiscussionFragment discussionFragment, String studentId) {
        this.context = context;
        this.discussionFragment = discussionFragment;
        this.studentId = studentId;
    }

    @Override
    public void run() {
        fetchMessages();
    }


    private void fetchMessages() {
        try {

            String urlResult = Uri.parse("http://192.168.0.7:8080/student/webapi/messages/")
                    .buildUpon()
                    .appendPath(studentId)
                    .build().toString();

            String result = getUrlString(urlResult);

            JSONArray jsonArray = new JSONArray(result);

            parseMessages(jsonArray);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseMessages(JSONArray jsonArray) throws JSONException {
        MessageController messageController = new MessageController(context);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            MessageContent messageContent = new MessageContent();
            messageContent.setMessageId(jsonObject.getString("messageId"));
            messageContent.setMessageContent(jsonObject.getString("messageContent"));
            messageContent.setStudentMessageBelongsTo(jsonObject.getString("sender"));
            messageContent.setMessageReceivedDate(new Date());

            messageController.insertMessage(messageContent);

            String removeMessageId = messageContent.getMessageId();

            //remember to remove the messages when received
            try {
                String messageReceived = Uri.parse("http://192.168.0.7:8080/student/webapi/messages/")
                        .buildUpon()
                        .appendPath(removeMessageId)
                        .build().toString();

                String result = getUrlString(messageReceived);
                Log.i(TAG, result);

            } catch (IOException e) {
                e.printStackTrace();
            }

            discussionFragment.returnDateFromThread();
        }


    }





    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream inputStream = httpURLConnection.getInputStream();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];

            while ((bytesRead = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }

            out.close();

            return out.toByteArray();

        } finally {
            httpURLConnection.disconnect();
        }
    }




    @SuppressLint("HandlerLeak")
    public Handler sendMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String sendMessageContent = bundle.getString("SendKey");


            try {

                String urlResult = Uri.parse("http://192.168.0.7:8080/student/webapi/sendMessages/")
                        .buildUpon()
                        .appendPath(sendMessageContent)
                        .build().toString();

                String result = getUrlString(urlResult);
                Log.i(TAG, result);

                String[] splitData = sendMessageContent.split("-");
                String content = splitData[0];
                String colleagueId = splitData[2];

                MessageContent messageContent = new MessageContent();
                messageContent.setMessageId(UUID.randomUUID().toString());
                messageContent.setMessageContent(content);
                messageContent.setStudentMessageBelongsTo(colleagueId);
                messageContent.setMessageReceivedDate(new Date());

                MessageController messageController = new MessageController(context);
                messageController.insertMessage(messageContent);

                fetchMessages();

                discussionFragment.returnDateFromThread();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };
}
