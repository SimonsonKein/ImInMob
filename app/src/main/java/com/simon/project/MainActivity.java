package com.simon.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bottomNavigationViewOptions();
        toolbarSettings();
    }

    private void toolbarSettings() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    private BottomNavigationView bottomNavigationView;
    private TextView toolbarTV;
    private void init() {
        bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        toolbarTV = findViewById(R.id.toolbar_tv);
    }

    private void bottomNavigationViewOptions() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_fragment_container_view);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        ImageButton notificationBtn = findViewById(R.id.account_notification_img_btn);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.fragment_main_friends:
                    toolbarTV.setText(R.string.friends);
                    break;
                case R.id.fragment_main_map:
                    toolbarTV.setText(R.string.map);
                    break;
                case R.id.fragment_main_chats:
                    toolbarTV.setText(R.string.chats);
                    break;
                case R.id.fragment_main_find:
                    toolbarTV.setText(R.string.find);
                    break;
                case R.id.fragment_main_account:
                    toolbarTV.setText(R.string.account);
                    break;
            }
            if (destination.getId() == R.id.fragment_main_account) {
                notificationBtn.setVisibility(View.VISIBLE);
            } else {
                notificationBtn.setVisibility(View.GONE);
            }
        });
        FirebaseFirestore.getInstance().collection("FriendsRequest")
                .whereEqualTo("friends_request", FirebaseAuth.getInstance().getUid())
                .addSnapshotListener((snapshots, error) -> {
                    assert snapshots != null;
                    for (DocumentChange documentChange : snapshots.getDocumentChanges()) {
                        switch (documentChange.getType())  {
                            case ADDED:
                                notificationBtn.setImageResource(R.drawable.ic_baseline_notifications_active_24);
                            case MODIFIED:
                                break;
                            case REMOVED:
                                if (snapshots.size() == 0) {
                                    notificationBtn.setImageResource(R.drawable.ic_baseline_notifications_none_24);
                                }
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + documentChange.getType());
                        }
                    }
                });
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