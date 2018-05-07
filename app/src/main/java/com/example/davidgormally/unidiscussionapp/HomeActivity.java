package com.example.davidgormally.unidiscussionapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.davidgormally.unidiscussionapp.controller.StudentController;
import com.example.davidgormally.unidiscussionapp.fragments.AccountFragment;
import com.example.davidgormally.unidiscussionapp.fragments.ColleagueListFragment;
import com.example.davidgormally.unidiscussionapp.fragments.MessageListFragment;
import com.example.davidgormally.unidiscussionapp.model.student.Student;

public class HomeActivity extends AppCompatActivity {

    private static final String arg = "id";
    private Student mStudent;

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(arg, id);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String id = (String)getIntent().getSerializableExtra(arg);
        StudentController studentController = new StudentController(this);
        mStudent = studentController.getStudent(id);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.messages));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.colleagues));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.account));

        final ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);
        final PagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

    private class TabPagerAdapter extends FragmentPagerAdapter {

        private int tabCount;

        private TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    return MessageListFragment.newInstance(mStudent.getId());
                case 1:
                    return ColleagueListFragment.newInstance();
                case 2:
                    return AccountFragment.newInstance(mStudent.getId());

                    default:
                        return null;
            }

        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

}
