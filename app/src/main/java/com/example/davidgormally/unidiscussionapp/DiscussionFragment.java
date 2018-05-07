package com.example.davidgormally.unidiscussionapp;


import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.davidgormally.unidiscussionapp.controller.ColleagueController;
import com.example.davidgormally.unidiscussionapp.controller.MessageController;
import com.example.davidgormally.unidiscussionapp.controller.StudentController;
import com.example.davidgormally.unidiscussionapp.model.colleague.Colleague;
import com.example.davidgormally.unidiscussionapp.model.message.MessageContent;
import com.example.davidgormally.unidiscussionapp.model.student.Student;
import com.example.davidgormally.unidiscussionapp.network.SendMessageTask;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscussionFragment extends Fragment {

    //colleague id
    private static final String arg = "id";
    private Colleague mColleague;
    private Student mStudent;
    private RecyclerView recyclerView;
    private EditText contentET;
    private SendMessageTask sendMessageTask;

    public static DiscussionFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putSerializable(arg, id);

        DiscussionFragment discussionFragment = new DiscussionFragment();
        discussionFragment.setArguments(args);

        return discussionFragment;
    }

    public DiscussionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = (String)getArguments().getSerializable(arg);
        ColleagueController colleagueController = new ColleagueController(getContext());
        mColleague = colleagueController.getColleague(id);

        StudentController studentController = new StudentController(getContext());
        List<Student> students = studentController.getStudents();

        String studentId = null;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).isSignedIn()) {
                studentId = students.get(i).getId();
            }
        }

        mStudent = studentController.getStudent(studentId);

        //start background task
        sendMessageTask = new SendMessageTask(getContext(), this, mStudent.getId());
        Thread thread = new Thread(sendMessageTask);
        thread.start();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discussion, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.discussion_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        contentET = (EditText)view.findViewById(R.id.discussion_content_edit_text);

        Button sendBtn = (Button)view.findViewById(R.id.send_button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        updateList();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

    private class DiscussionListAdapter extends RecyclerView.Adapter<DiscussionListAdapter.DiscussionListHolder> {

        private List<MessageContent> messageContents;

        public DiscussionListAdapter(List<MessageContent> messageContents) {
            this.messageContents = messageContents;
        }

        @NonNull
        @Override
        public DiscussionListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
            return new DiscussionListHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DiscussionListHolder holder, int position) {
           MessageContent messageContent = messageContents.get(position);
           holder.bindMessageContent(messageContent);
        }

        @Override
        public int getItemCount() {
            return messageContents.size();
        }

        class DiscussionListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView dateTv;
            private TextView contentTv;
            private MessageContent mMessageContent;

            private DiscussionListHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);

                dateTv = (TextView)itemView.findViewById(R.id.date_content_text_view);
                contentTv = (TextView)itemView.findViewById(R.id.message_content_text_view);
            }

            private void bindMessageContent(MessageContent messageContent) {
                mMessageContent = messageContent;

                long date = messageContent.getMessageReceivedDate().getTime();
                String format = new SimpleDateFormat("MM/dd/yyyy").format(date);
                dateTv.setText(format);

                contentTv.setText(mMessageContent.getMessageContent());
            }

            @Override
            public void onClick(View v) {

            }
        }
    }

    private void updateList() {
        if (isAdded()) {

            MessageController messageController = new MessageController(getContext());
            List<MessageContent> messageContents = messageController.getMessages(mColleague.getId());

            recyclerView.setAdapter(new DiscussionListAdapter(messageContents));
        }
    }

    private void sendMessage() {
        String content = contentET.getText().toString();
        String studentId = mStudent.getId();
        String colleagueId = mColleague.getId();

        String result = content + "-" + studentId + "-" + colleagueId;

        Message message = sendMessageTask.sendMessageHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("SendKey", result);
        message.setData(bundle);

        sendMessageTask.sendMessageHandler.sendMessage(message);
    }

    public void returnDateFromThread() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateList();
            }
        });
    }
}
