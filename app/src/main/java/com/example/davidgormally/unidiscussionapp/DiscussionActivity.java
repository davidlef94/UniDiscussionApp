package com.example.davidgormally.unidiscussionapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class DiscussionActivity extends AppCompatActivity {

    private static final String arg = "colleagueId";

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, DiscussionActivity.class);
        intent.putExtra(arg, id);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        String id = (String)getIntent().getSerializableExtra(arg);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container_frag);


        if (fragment == null) {
            fragment = DiscussionFragment.newInstance(id);
            fragmentManager.beginTransaction().add(R.id.container_frag, fragment).commit();
        }
    }
}
