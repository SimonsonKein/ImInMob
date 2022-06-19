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

import com.simon.project.R;
import com.simon.project.controllers.FirebaseEventController;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindUsersAdapter extends RecyclerView.Adapter<FindUsersAdapter.ViewHolder> {

    Context context;
    List<String> usersIDList;

    public FindUsersAdapter(Context context, Activity activity, List<String> usersIDList) {
        this.context = context;
        this.usersIDList = usersIDList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_find_users, parent, false);
        return new FindUsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseEventController.setUsersNicknameTVByID(usersIDList.get(position), holder.userNicknameTV);
        String thisUID = usersIDList.get(position);
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
        return usersIDList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNicknameTV;
        public CircleImageView avatar;
        public ViewHolder(View view) {
            super(view);
            userNicknameTV = view.findViewById(R.id.find_nickname_tv);
            avatar = view.findViewById(R.id.find_friends_adapter_item_avatar_iv);
        }
        public void bind() {
        }
    }
}