package com.example.davidgormally.unidiscussionapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davidgormally.unidiscussionapp.R;
import com.example.davidgormally.unidiscussionapp.SignInActivity;
import com.example.davidgormally.unidiscussionapp.controller.StudentController;
import com.example.davidgormally.unidiscussionapp.model.student.Student;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private static final String arg = "";
    private Student mStudent;

    public static AccountFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putSerializable(arg, id);

        AccountFragment accountFragment = new AccountFragment();
        accountFragment.setArguments(args);

        return accountFragment;
    }


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = (String)getArguments().getSerializable(arg);
        StudentController studentController = new StudentController(getContext());
        mStudent = studentController.getStudent(id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        TextView textView = (TextView)view.findViewById(R.id.name_account_text_view);
        textView.setText(mStudent.getUserName());

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.account_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recyclerView.setAdapter(new AccountAdapter());

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }




    private class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {



        private AccountAdapter() {
        }

        @NonNull
        @Override
        public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_card_item, parent, false);
            return new AccountHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AccountHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class AccountHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private AccountHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
            }

            private void bindAccountCard() {

            }

            @Override
            public void onClick(View v) {

            }
        }
    }




    private void enterDetailsFragment() {

    }

    private void signOutUser() {
        mStudent.setSignedIn(false);
        StudentController studentController = new StudentController(getContext());
        studentController.updateStudent(mStudent);

        Intent intent = new Intent(getContext(), SignInActivity.class);
        startActivity(intent);

        //getActivity().finish();
    }

}
