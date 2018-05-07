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
import com.example.davidgormally.unidiscussionapp.controller.MessageController;
import com.example.davidgormally.unidiscussionapp.controller.StudentController;
import com.example.davidgormally.unidiscussionapp.model.colleague.Colleague;
import com.example.davidgormally.unidiscussionapp.model.message.MessageContent;
import com.example.davidgormally.unidiscussionapp.model.student.Student;
import com.example.davidgormally.unidiscussionapp.network.UpdateMessageListTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageListFragment extends Fragment {

    private static final String arg = "id";
    private RecyclerView recyclerView;
    private Student mStudent;

    public static MessageListFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(arg, id);

        MessageListFragment messageListFragment = new MessageListFragment();
        messageListFragment.setArguments(args);

        return messageListFragment;
    }

    public MessageListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = (String)getArguments().getSerializable(arg);
        StudentController studentController = new StudentController(getContext());
        mStudent = studentController.getStudent(id);

        //set service
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.message_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateList();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        //Thread
        UpdateMessageListTask updateMessageListTask =
                new UpdateMessageListTask(getContext(), this, mStudent.getId());

        Thread thread = new Thread(updateMessageListTask);
        thread.start();
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


    private class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListHolder> {

        private List<Colleague> colleagues;

        public MessageListAdapter(List<Colleague> colleagues) {
            this.colleagues = colleagues;
        }

        @NonNull
        @Override
        public MessageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item, parent, false);
            return new MessageListHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MessageListHolder holder, int position) {
            Colleague colleague = colleagues.get(position);
            holder.bindMessage(colleague);
        }

        @Override
        public int getItemCount() {
            return colleagues.size();
        }

        class MessageListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private Colleague mColleague;
            private TextView nameTv;
            private TextView messageTv;
            private TextView dateTv;

            private MessageListHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);

                nameTv = (TextView)itemView.findViewById(R.id.colleague_name_text_view);
                messageTv = (TextView)itemView.findViewById(R.id.colleague_content_text_view);
                dateTv = (TextView)itemView.findViewById(R.id.message_date_text_view);
            }

            private void bindMessage(Colleague colleague) {
                mColleague = colleague;

                MessageController messageController = new MessageController(getContext());
                List<MessageContent> messageContents = messageController.getMessages(mColleague.getId());

                int lastMessage = messageContents.size() - 1;

                String messageContent = messageContents.get(lastMessage).getMessageContent();

                long date = messageContents.get(lastMessage).getMessageReceivedDate().getTime();
                String format = new SimpleDateFormat("MM/dd/yyyy").format(date);

                messageTv.setText(messageContent);
                dateTv.setText(format);
            }

            @Override
            public void onClick(View v) {
                Intent intent = DiscussionActivity.newIntent(getContext(), mColleague.getId());
                startActivity(intent);
            }
        }
    }


    private void updateList() {
        if (isAdded()) {
            List<Colleague> colleaguesList = new ArrayList<>();
            List<String> colleagueIds = new ArrayList<>();

            ColleagueController colleagueController = new ColleagueController(getContext());
            List<Colleague> colleagues = colleagueController.getColleagues();

            for (int i = 0; i < colleagues.size(); i++) {
                String colleagueId = colleagues.get(i).getId();
                colleagueIds.add(colleagueId);
            }

            List<String> messageColleagueIds = new ArrayList<>();

            MessageController messageController = new MessageController(getContext());
            List<MessageContent> messageContents = messageController.getAllMessages();

            for (int i = 0; i < messageContents.size(); i++) {
                String colleagueId = messageContents.get(i).getStudentMessageBelongsTo();
                messageColleagueIds.add(colleagueId);
            }

            for (int i = 0; i < colleagueIds.size(); i++) {
                if (messageColleagueIds.contains(colleagueIds.get(i))) {
                    String colleagueId = colleagueIds.get(i);
                    Colleague colleague = colleagueController.getColleague(colleagueId);
                    colleaguesList.add(colleague);
                }
            }

            recyclerView.setAdapter(new MessageListAdapter(colleaguesList));
        }
    }

    public void updateFromThread() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateList();
            }
        });
    }

}
