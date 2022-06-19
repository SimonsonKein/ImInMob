package com.simon.project.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.simon.project.model.MessageModel;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context context;
    List<MessageModel> messagesList;

    int SENDER_ITEM = 1;
    int RECEIVER_ITEM = 2;

    public MessageAdapter(Context context, Activity activity, List<MessageModel> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == SENDER_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_chat_sender_message_item, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_chat_receiver_message_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.messageTextTV.setText(messagesList.get(position).getMessage());
       holder.setMessageNicknameTV(messagesList.get(position).getSenderID(), holder.messageNicknameTV);
        String thisUID = messagesList.get(position).getSenderID();
        switch (thisUID) {
            case "IKpoZKuWKjfAYx230EWjzZczbOx1"://Alexandr
                holder.messageIV.setImageResource(R.drawable.alex);
                break;
            case "npSAUa7r0wQQ6dtqRDYr862Q3Dw1":
                holder.messageIV.setImageResource(R.drawable.dima);
                break;
            case "DXYosr39b9ef8cWO4yeOa8ggVAB2":
                holder.messageIV.setImageResource(R.drawable.nastea);
                break;
            case "8L87L1WzzTS3P7TeenWsXMogv4V2":
                holder.messageIV.setImageResource(R.drawable.max);
                break;
            case "XFVZfLTOynhzNlQt5NsHZm4F2Zu1":
                holder.messageIV.setImageResource(R.drawable.kassa);
                break;
            case "ttJLzntZuEPZQCLvD3nFRSSdud73":
                holder.messageIV.setImageResource(R.drawable.dima);
                break;
            case "vAsPQb8vFge6H7PymILgrynB8Nb2":
                holder.messageIV.setImageResource(R.drawable.daria);
                break;
            case "w2ONKNTeFqRiEoGlkiCqgAtMeJS2":
                holder.messageIV.setImageResource(R.drawable.salagub);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        String UID = FirebaseAuth.getInstance().getUid();
        if (messagesList.get(position).getSenderID().equals(UID)) {
            return SENDER_ITEM;
        } else {
            return RECEIVER_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextTV;
        public TextView messageNicknameTV;
        public TextView messageTimeTV;
        public ImageView messageIV;
        public ViewHolder(View view) {
            super(view);
            messageTextTV = view.findViewById(R.id.message_text_tv);
            messageNicknameTV = view.findViewById(R.id.message_nickname_tv);
            messageIV = view.findViewById(R.id.message_iv);
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