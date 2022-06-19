package com.simon.project.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.simon.project.R;
import com.simon.project.controllers.FirebaseEventController;
import com.simon.project.model.EventCategory;
import com.simon.project.utils.Image;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    Context context;
    List<String> friendsList;

    public FriendsAdapter(Context context, Activity activity, List<String> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_friends, parent, false);
        return new FriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseEventController.setFriendNameTV(friendsList, holder.friendNameTV, position);
        String thisUID = friendsList.get(position);
        switch (thisUID) {
            case "IKpoZKuWKjfAYx230EWjzZczbOx1"://Alexandr
                holder.avatar.setImageResource(R.drawable.alex);
                break;
            case "npSAUa7r0wQQ6dtqRDYr862Q3Dw1":
                holder.avatar.setImageResource(R.drawable.dima);
                break;
            case "DXYosr39b9ef8cWO4yeOa8ggVAB2":
                holder.avatar.setImageResource(R.drawable.nastea);
                break;
            case "8L87L1WzzTS3P7TeenWsXMogv4V2":
                holder.avatar.setImageResource(R.drawable.max);
                break;
            case "XFVZfLTOynhzNlQt5NsHZm4F2Zu1":
                holder.avatar.setImageResource(R.drawable.kassa);
                break;
            case "ttJLzntZuEPZQCLvD3nFRSSdud73":
                holder.avatar.setImageResource(R.drawable.dima);
                break;
            case "vAsPQb8vFge6H7PymILgrynB8Nb2":
                holder.avatar.setImageResource(R.drawable.daria);
                break;
            case "w2ONKNTeFqRiEoGlkiCqgAtMeJS2":
                holder.avatar.setImageResource(R.drawable.salagub);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout friendsAdapterItem;
        public TextView friendNameTV;
        public CircleImageView avatar;

        public ViewHolder(View view) {
            super(view);
            friendNameTV = view.findViewById(R.id.friends_adapter_item_name_tv);
            friendsAdapterItem = view.findViewById(R.id.friends_adapter_item);
            avatar = view.findViewById(R.id.friends_adapter_item_avatar_iv);

        }


        public void bind() {

        }
    }
}