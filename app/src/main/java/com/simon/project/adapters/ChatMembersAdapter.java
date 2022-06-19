package com.simon.project.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.simon.project.R;
import com.simon.project.controllers.FirebaseEventController;
import com.simon.project.model.MessageModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMembersAdapter extends RecyclerView.Adapter<ChatMembersAdapter.ViewHolder> {

    Context context;
    List<String> UIDList;

    public ChatMembersAdapter(Context context, List<String> UIDList) {
        this.context = context;
        this.UIDList = UIDList;
        Log.d("UIDLIST_SIZE", String.valueOf(UIDList.size()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_chat_members, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseEventController.setUsersNicknameTVByID(UIDList.get(position), holder.memberNicknameTV);
        String thisUID = UIDList.get(position);
        switch (thisUID) {
            case "IKpoZKuWKjfAYx230EWjzZczbOx1"://Alexandr
                holder.memberIV.setImageResource(R.drawable.alex);
                break;
            case "npSAUa7r0wQQ6dtqRDYr862Q3Dw1":
                holder.memberIV.setImageResource(R.drawable.dima);
                break;
            case "DXYosr39b9ef8cWO4yeOa8ggVAB2":
                holder.memberIV.setImageResource(R.drawable.nastea);
        }
    }

    @Override
    public int getItemCount() {
        return UIDList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView memberNicknameTV;
        public CircleImageView memberIV;
        public ViewHolder(View view) {
            super(view);
            memberNicknameTV = view.findViewById(R.id.chat_info_member_nickname_tv);
            memberIV = view.findViewById(R.id.member_iv);
        }
        public void bind() {
        }
        public void setMessageNicknameTV (String UID, TextView messageNicknameTV) {
            FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(UID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            messageNicknameTV.setText((String) task.getResult().get("nickname"));
                        }
                    });
        }
    }
}