package com.example.davidgormally.unidiscussionapp.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.davidgormally.unidiscussionapp.controller.MessageController;
import com.example.davidgormally.unidiscussionapp.fragments.MessageListFragment;
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

public class UpdateMessageListTask implements Runnable {

    private static final String TAG = "UpdateMessageList";
    private Context context;
    private MessageListFragment messageListFragment;
    private String studentId;

    public UpdateMessageListTask(Context context, MessageListFragment messageListFragment, String studentId) {
        this.context = context;
        this.messageListFragment = messageListFragment;
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

            messageListFragment.updateFromThread();
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
}
