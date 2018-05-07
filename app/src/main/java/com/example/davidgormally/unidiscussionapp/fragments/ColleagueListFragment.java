package com.example.davidgormally.unidiscussionapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davidgormally.unidiscussionapp.DiscussionActivity;
import com.example.davidgormally.unidiscussionapp.R;
import com.example.davidgormally.unidiscussionapp.controller.ColleagueController;
import com.example.davidgormally.unidiscussionapp.model.colleague.Colleague;
import com.example.davidgormally.unidiscussionapp.network.FetchColleagueTask;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColleagueListFragment extends Fragment {

    private RecyclerView recyclerView;

    public static ColleagueListFragment newInstance() {
        return new ColleagueListFragment();
    }


    public ColleagueListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_colleague_list, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.colleague_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateColleagueList();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        FetchColleagueTask fetchColleagueTask = new FetchColleagueTask(getContext(), this);
        Thread thread = new Thread(fetchColleagueTask);
        thread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateColleagueList();
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


    private class ColleagueListAdapter extends RecyclerView.Adapter<ColleagueListAdapter.ColleagueListHolder> {

        private List<Colleague> colleagues;

        public ColleagueListAdapter(List<Colleague> colleagues) {
            this.colleagues = colleagues;
        }

        @NonNull
        @Override
        public ColleagueListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.colleague_item, parent, false);
            return new ColleagueListHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ColleagueListHolder holder, int position) {
            Colleague colleague = colleagues.get(position);
            holder.bindColleague(colleague);
        }

        @Override
        public int getItemCount() {
            return colleagues.size();
        }

        class ColleagueListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private Colleague mColleague;
            private TextView nameTv;
            private TextView emailTv;
            //image view later

            private ColleagueListHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);

                nameTv = (TextView)itemView.findViewById(R.id.colleague_first_name_text_view);
                emailTv = (TextView)itemView.findViewById(R.id.colleague_email_text_view);
            }

            private void bindColleague(Colleague colleague) {
                mColleague = colleague;
                nameTv.setText(mColleague.getFirstName() + " " + mColleague.getLastName());
                emailTv.setText(mColleague.getEmail());
            }

            @Override
            public void onClick(View v) {
                Intent intent = DiscussionActivity.newIntent(getContext(), mColleague.getId());
                startActivity(intent);
            }
        }
    }

    private void updateColleagueList() {
        if (isAdded()) {
            ColleagueController colleagueController = new ColleagueController(getContext());
            List<Colleague> colleagues = colleagueController.getColleagues();

            recyclerView.setAdapter(new ColleagueListAdapter(colleagues));
        }
    }

    public void returnColleagueDetails() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateColleagueList();
            }
        });
    }
}
