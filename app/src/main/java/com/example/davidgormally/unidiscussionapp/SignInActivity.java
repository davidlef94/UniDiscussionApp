package com.example.davidgormally.unidiscussionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.davidgormally.unidiscussionapp.controller.StudentController;
import com.example.davidgormally.unidiscussionapp.model.student.Student;
import com.example.davidgormally.unidiscussionapp.network.SignInTask;

import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private EditText userNameEt;
    private EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userNameEt = (EditText)findViewById(R.id.username_edit_text);
        passwordEt = (EditText)findViewById(R.id.password_edit_text);

        Button signInButton = (Button)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signStudentIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void signStudentIn() {
        String username = userNameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String result = username + "-" + password;

        if ((username.equals("")) && (password.equals(""))) {
            Toast.makeText(this, "Please Enter Valid Data", Toast.LENGTH_LONG).show();
        }

        SignInTask signInTask = new SignInTask(this, result, this);
        Thread thread = new Thread(signInTask);
        thread.start();
    }

    public void signInProcessResult(String result) {
        if (result.equals("Failure")) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
            });

        } else {

            Student student = null;

            StudentController studentController = new StudentController(this);
            List<Student> students = studentController.getStudents();

            for (int i = 0; i < students.size(); i++) {
                student = students.get(i);
            }

            Intent intent = HomeActivity.newIntent(this, student.getId());
            startActivity(intent);
        }
    }

}
