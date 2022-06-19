package com.simon.project.account_fragment_activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.simon.project.R;
import com.simon.project.adapters.NotificationAdapter;
import com.simon.project.controllers.FirebaseEventController;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
        toolbarSettings();
    }

    private void init() {
        RecyclerView notificationRV = findViewById(R.id.notification_rv);
        notificationRV.setHasFixedSize(true);
        notificationRV.setLayoutManager(new LinearLayoutManager(this));
        List<String> friendsRequestList = new ArrayList<>();
        NotificationAdapter notificationAdapter = new NotificationAdapter(this, this, friendsRequestList);
        notificationRV.setAdapter(notificationAdapter);
        FirebaseEventController.setNotificationListAdapter(friendsRequestList, notificationAdapter);
    }

    private void toolbarSettings() {
        Toolbar toolbar = findViewById(R.id.notification_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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