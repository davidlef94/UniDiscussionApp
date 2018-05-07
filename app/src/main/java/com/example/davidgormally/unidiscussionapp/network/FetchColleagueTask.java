package com.example.davidgormally.unidiscussionapp.network;

import android.content.Context;

import com.example.davidgormally.unidiscussionapp.controller.ColleagueController;
import com.example.davidgormally.unidiscussionapp.fragments.ColleagueListFragment;
import com.example.davidgormally.unidiscussionapp.model.colleague.Colleague;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FetchColleagueTask implements Runnable {

    private ColleagueListFragment colleagueListFragment;
    private Context context;

    public FetchColleagueTask(Context context, ColleagueListFragment colleagueListFragment) {
        this.context = context;
        this.colleagueListFragment = colleagueListFragment;
    }

    @Override
    public void run() {
        fetchColleagues();
    }

    private void fetchColleagues() {

        try {

            String result = getUrlString("http://192.168.0.7:8080/student/webapi/students");
            JSONArray jsonArray = new JSONArray(result);

            parseColleagues(jsonArray);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseColleagues(JSONArray jsonArray) throws JSONException {

        ColleagueController colleagueController = new ColleagueController(context);
        List<Colleague> colleagues = colleagueController.getColleagues();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (!colleagues.contains(jsonObject.getString("studentId"))) {
                Colleague colleague = new Colleague();
                colleague.setId(jsonObject.getString("studentId"));
                colleague.setFirstName(jsonObject.getString("studentFirstName"));
                colleague.setLastName(jsonObject.getString("studentLastName"));
                colleague.setEmail(jsonObject.getString("studentEmail"));

                colleagueController.insertColleague(colleague);
            }
        }

        //update UI
        colleagueListFragment.returnColleagueDetails();
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
