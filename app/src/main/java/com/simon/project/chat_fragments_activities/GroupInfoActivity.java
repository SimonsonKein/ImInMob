package com.simon.project.chat_fragments_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.simon.project.R;
import com.simon.project.adapters.MessageAdapter;
import com.simon.project.controllers.FirebaseEventController;
import com.simon.project.model.EventInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        EventInfo eventInfo = (EventInfo) getIntent().getSerializableExtra("event_info");
        if (eventInfo != null) {
            setTitleToolbar();
            init(eventInfo.getEventID());
            Log.d("GROUP_INFO", eventInfo.getEventID() + "\n" + eventInfo.getTitle());
            recyclerViewOptions(eventInfo.getEventID());
        }
    }

    CircleImageView groupIcoCiv;
    TextView TitleTv, CountMembersTv, CategoryTv, DateTv, TimeTv, DescriptionTv;

    private void init(String eventId) {

        groupIcoCiv = findViewById(R.id.group_info_icon_civ);
        TitleTv = findViewById(R.id.group_info_title_tv);
        CountMembersTv = findViewById(R.id.group_info_count_members_tv);
        CategoryTv = findViewById(R.id.group_info_category_tv);
        DateTv = findViewById(R.id.group_info_date_tv);
        TimeTv = findViewById(R.id.group_info_time_tv);
        DescriptionTv = findViewById(R.id.group_info_description_tv);

        FirebaseFirestore.getInstance()
                .collection("Events")
                .document(eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        switch ((String) task.getResult().get("category")) {
                            case "Sport":
                                groupIcoCiv.setImageResource(R.drawable.ic_dumbbell_training_svgrepo_com);
                                break;
                            case "Pool":
                                groupIcoCiv.setImageResource(R.drawable.ic_swimming_pool_svgrepo_com);
                                break;
                            case "Walk":
                                groupIcoCiv.setImageResource(R.drawable.ic_running_shoes_8592);
                                break;
                            case "Art":
                                groupIcoCiv.setImageResource(R.drawable.ic_artist_color_palette_svgrepo_com);
                                break;
                            case "Party":
                                groupIcoCiv.setImageResource(R.drawable.ic_party_blower_birthday_svgrepo_com);
                                break;
                        }
                        TitleTv.setText((String) task.getResult().get("title"));
                        CountMembersTv.setText("Members " + task.getResult().get("current_amount_members").toString() + "/" + task.getResult().get("amount_members").toString());
                        CategoryTv.setText((String) task.getResult().get("category"));
                        DateTv.setText((String) task.getResult().get("date"));
                        TimeTv.setText((String) task.getResult().get("time"));
                        DescriptionTv.setText((String) task.getResult().get("description"));
                    }
                });
    }

    private void setTitleToolbar() {
        Toolbar toolbar = findViewById(R.id.chat_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView toolbarTV = findViewById(R.id.chat_info_toolbar_tv);
        toolbarTV.setText("Event info");
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

    RecyclerView recyclerView;
    private void recyclerViewOptions(String eventID) {
        recyclerView = findViewById(R.id.group_info_members_rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseEventController.setChatMemberListAdapter(
                this,
                eventID,
                recyclerView);
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