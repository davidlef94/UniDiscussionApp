package com.example.davidgormally.unidiscussionapp.network;

import android.content.Context;
import android.net.Uri;


import com.example.davidgormally.unidiscussionapp.SignInActivity;
import com.example.davidgormally.unidiscussionapp.controller.StudentController;
import com.example.davidgormally.unidiscussionapp.model.student.Student;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SignInTask implements Runnable {

    private Context context;
    private SignInActivity signInActivity;
    private String userNamePassword;

    public SignInTask(Context context, String userNamePassword, SignInActivity signInActivity) {
        this.context = context;
        this.userNamePassword = userNamePassword;
        this.signInActivity = signInActivity;
    }

    @Override
    public void run() {
        //POST
        signInActivity.signInProcessResult(getStudentDetails());
    }

    private String getStudentDetails() {

        try {
            String urlResult = Uri.parse("http://192.168.0.7:8080/student/webapi/signIns/")
                    .buildUpon()
                    .appendPath(userNamePassword)
                    .build().toString();

            String jsonString = getUrlString(urlResult);

            JSONObject jsonObject = new JSONObject(jsonString);

            return getStudentFromJsonArray(jsonObject);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return "Failure";
    }


    private String getStudentFromJsonArray(JSONObject jsonObject) throws IOException, JSONException {

        StudentController studentController = new StudentController(context);
        List<Student> students = studentController.getStudents();

        String studentId = jsonObject.getString("studentId");

        if (jsonObject.getString("studentId").equals("")) {

            return "Failure";

        } else if (!students.isEmpty()) {

            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId().equals(studentId)) {
                    students.get(i).setSignedIn(true);
                    studentController.updateStudent(students.get(i));

                    return "Success";
                }
            }

        } else {

            Student student = new Student();
            student.setId(jsonObject.getString("studentId"));
            student.setFirstName(jsonObject.getString("studentFirstName"));
            student.setLastName(jsonObject.getString("studentLastName"));
            student.setUserName(jsonObject.getString("studentUsername"));
            student.setEmail(jsonObject.getString("studentEmail"));
            student.setSignedIn(true);

            studentController.addStudent(student);

            return "Success";
        }

        return "Failure";
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
