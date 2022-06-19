package com.simon.project.adapters;

import static com.simon.project.controllers.FirebaseEventController.declineRequest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.simon.project.R;
import com.simon.project.controllers.FirebaseEventController;

import java.util.HashMap;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    List<String> friendsRequestList;

    public NotificationAdapter(Context context, Activity activity, List<String> friendsRequestList) {
        this.context = context;
        this.friendsRequestList = friendsRequestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_notification, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseEventController.setFriendRequestNameTV(friendsRequestList, holder.requestNicknameTV, position);
        holder.clickListener(position);
    }

    @Override
    public int getItemCount() {
        return friendsRequestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView requestNicknameTV;
        public ImageButton acceptRequestBtn;
        public ImageButton declineRequestBtn;
        public ViewHolder(View view) {
            super(view);
            requestNicknameTV = view.findViewById(R.id.notification_adapter_item_name_tv);
            acceptRequestBtn = view.findViewById(R.id.request_accept_btn);
            declineRequestBtn = view.findViewById(R.id.request_decline_btn);
        }
        public void bind() {
        }

        public void clickListener(int position) {
            acceptRequestBtn.setOnClickListener(view -> {
                FirebaseEventController.acceptFriend(friendsRequestList, position, NotificationAdapter.this);
            });

            declineRequestBtn.setOnClickListener(view -> {
                FirebaseEventController.declineRequest(friendsRequestList, position, NotificationAdapter.this);
            });
        }
    }



}