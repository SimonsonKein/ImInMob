package com.simon.project.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simon.project.R;
import com.simon.project.controllers.FirebaseEventController;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    Context context;
    List<String> eventsIdList;

    public ChatsAdapter(Context context, Activity activity, List<String> eventsIdList) {
        this.context = context;
        this.eventsIdList = eventsIdList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_chats, parent, false);
        return new ChatsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseEventController.setChatNameTVAndLastMessage(eventsIdList, holder.chatNameTV, holder.lastMessageTV, position);
    }

    @Override
    public int getItemCount() {
        return eventsIdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chatNameTV;
        public TextView lastMessageTV;
        public ViewHolder(View view) {
            super(view);
            chatNameTV = view.findViewById(R.id.chats_adapter_item_name_tv);
            lastMessageTV = view.findViewById(R.id.chats_adapter_item_last_message_tv);
        }
        public void bind() {
        }
    }
}