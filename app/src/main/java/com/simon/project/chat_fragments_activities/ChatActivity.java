package com.simon.project.chat_fragments_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.simon.project.R;
import com.simon.project.adapters.MessageAdapter;
import com.simon.project.model.EventInfo;
import com.simon.project.model.MessageModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        EventInfo eventInfo = (EventInfo) getIntent().getSerializableExtra("event_info");
        if (eventInfo != null) {
            String eventId = eventInfo.getEventID();
            String eventTitle = eventInfo.getTitle();
            Log.d("EVENT_INFO", eventId+"\n"+eventTitle);
            setTitleToolbar(eventTitle);
            recyclerViewOptions();
            clickListener(eventInfo);
            updateOnNewMessage(eventId);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void recyclerViewOptions() {
        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(this, this, messageList);
        recyclerView.setAdapter(messageAdapter);
    }

    List<MessageModel> messageList = new ArrayList<>();
    MessageAdapter messageAdapter = new MessageAdapter(this, this, messageList);
    RecyclerView recyclerView;

    private void updateOnNewMessage(String eventId) {
        FirebaseFirestore.getInstance()
                .collection("Events")
                .document(eventId)
                .collection("Messages")
                .orderBy("date_time")
                .addSnapshotListener((snapshots, error) -> {
//                    messageList.clear();
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        String message = (String) dc.getDocument().get("message");
                        String senderID = (String) dc.getDocument().get("sender");
                        MessageModel messageModel = new MessageModel(message, senderID);
                        messageList.add(messageModel);
//                                Log.d("ADDED_MESSAGE", dc.getType().toString());
//                                Log.d("ADDED_MESSAGE", dc.toString());
                    }
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                });
    }



//    private void showAllMessages(String eventId) {
//        FirebaseFirestore.getInstance()
//                .collection("Events")
//                .document(eventId)
//                .collection("Messages").orderBy("date_time")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//
//                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
//                            messageList.add((String) documentSnapshot.get("message"));
//                        }
//
//                    }
//                });
//    }

    private void clickListener(EventInfo eventInfo) {
        ImageButton sendMessageBtn = findViewById(R.id.chat_send_btn);
        sendMessageBtn.setOnClickListener(view -> {
            EditText messageET = findViewById(R.id.chat_message_et);
            String message = messageET.getText().toString();
            messageET.setText("");
            if (!message.isEmpty()) {
                String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());
                HashMap<String, String> map = new HashMap<>();
                map.put("sender", FirebaseAuth.getInstance().getUid());
                map.put("message", message);
                map.put("date_time", date);
                FirebaseFirestore.getInstance()
                        .collection("Events")
                        .document(eventInfo.getEventID())
                        .collection("Messages")
                        .document()
                        .set(map)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("SAVE_MESSAGE", "Successful");
                                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                            } else {
                                Log.d("SAVE_MESSAGE", task.getException().toString());
                            }
                        });
            }
        });

        ImageButton groupInfoBtn = findViewById(R.id.chat_group_info_btn);
        groupInfoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, GroupInfoActivity.class);
            intent.putExtra("event_info", eventInfo);
            startActivity(intent);
        });
    }



    private void setTitleToolbar(String eventTitle) {
        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView toolbarTV = findViewById(R.id.chat_toolbar_tv);
//        FirebaseEventController.setChatTitleToolbar()
        toolbarTV.setText(eventTitle);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String status = "online";
        if (FirebaseAuth.getInstance().getUid() != null)
            FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .update("status", status);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String status = "offline";
        if (FirebaseAuth.getInstance().getUid() != null)
            FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .update("status", status);
    }
}

