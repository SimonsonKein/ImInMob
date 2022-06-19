package com.simon.project.bottom_model_fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.simon.project.R;
import com.simon.project.chat_fragments_activities.ChatActivity;
import com.simon.project.model.EventInfo;
import com.simon.project.utils.Image;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class JoinEventBottomSheet extends BottomSheetDialogFragment {

    String eventId;
    View rootView;
    Activity activity;
    public JoinEventBottomSheet(String eventId, View rootView, Activity activity) {
        this.rootView = rootView;
        this.eventId = eventId;
        this.activity = activity;
    }

    TextView eventTitleTv,
            eventDateTv,
            eventTimeTv,
            eventAmountMembersTv,
            eventDescriptionTv,
            eventCategoryTv;
    Button joinEventBtn;
    ImageView groupIconIv;
    Long maxAmountMembers;
    Long currentAmountMembers;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_event_bottom_sheet, container, false);

        eventTitleTv = view.findViewById(R.id.join_event_title_tv);
        eventDateTv = view.findViewById(R.id.join_event_date_tv);
        eventTimeTv = view.findViewById(R.id.join_event_time_tv);
        eventAmountMembersTv = view.findViewById(R.id.join_event_amount_members_tv);
        eventCategoryTv = view.findViewById(R.id.join_event_category_tv);
        eventDescriptionTv = view.findViewById(R.id.join_event_description_tv);
        groupIconIv = view.findViewById(R.id.group_icon_iv);
        joinEventBtn = view.findViewById(R.id.join_event_btn);

        checkUserConnectionGroup();
        getAllData();
        init();
        clickListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {

    }

    boolean isInGroup;
    boolean canBeJoined;

    private void checkUserConnectionGroup() {
        FirebaseFirestore.getInstance().collection("UsersEvents")
                .whereEqualTo("user_id", FirebaseAuth.getInstance().getUid())
                .whereEqualTo("event_id", eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        isInGroup = !task.getResult().isEmpty();
                    }
                });
    }



    private void clickListener() {
        joinEventBtn.setOnClickListener(v -> {
            String eventTitle = eventTitleTv.getText().toString();
            EventInfo eventInfo = new EventInfo(eventId, eventTitle);
            if (isInGroup) {
                Navigation.findNavController(rootView).navigate(R.id.fragment_main_chats);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("event_info", eventInfo);
                startActivity(intent);
            } else if (canBeJoined) {
                final String UID = FirebaseAuth.getInstance().getUid();

                HashMap<String, String> map = new HashMap<>();
                map.put("event_id", eventId);
                map.put("user_id", UID);
                FirebaseFirestore.getInstance().collection("UsersEvents").document()
                        .set(map)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseFirestore.getInstance().collection("Events").document(eventId)
                                        .update("current_amount_members", currentAmountMembers).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        currentAmountMembers++;
                                        Intent intent = new Intent(getContext(), ChatActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        intent.putExtra("event_info", eventInfo);
                                        Navigation.findNavController(rootView).navigate(R.id.fragment_main_chats);
                                        startActivity(intent);
                                        Log.d("JOIN_EVENT", "Successfully");
                                        Log.d("EVENTS_CHANGES", "Changes successfully");
                                    } else {
                                        Log.d("EVENTS_CHANGES", Objects.requireNonNull(task1.getException()).toString());
                                    }
                                });

                            } else {
                                Log.d("JOIN_EVENT", Objects.requireNonNull(task.getException()).toString());
                            }
                        });
            }
            this.dismiss();
        });
    }

    private void getAllData() {
        FirebaseFirestore.getInstance().collection("Events").document(eventId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        maxAmountMembers = (Long) Objects.requireNonNull(task.getResult().get("amount_members"));
                        currentAmountMembers = (Long) Objects.requireNonNull(task.getResult().get("current_amount_members"));
                        eventTitleTv.setText((String) task.getResult().get("title"));
                        eventDateTv.setText((String) task.getResult().get("date"));
                        eventTimeTv.setText((String) task.getResult().get("time"));
                        eventAmountMembersTv.setText(currentAmountMembers + "/" + maxAmountMembers);
                        eventCategoryTv.setText((String) task.getResult().get("category"));
                        eventDescriptionTv.setText((String) task.getResult().get("description"));
                        canBeJoined = !Objects.equals((Long) task.getResult().get("current_amount_members"), (Long) task.getResult().get("amount_members"));
                        setJoinBtnType(isInGroup, canBeJoined);
                        Log.d("ON_START", "1");
                        Log.d("ON+START_GROUP_TITLE", task.getResult().get("title").toString());
                        switch (eventCategoryTv.getText().toString()) {
                            case "Sport":
                                groupIconIv.setImageResource(R.drawable.ic_dumbbell_training_svgrepo_com);
                                break;
                            case "Pool":
                                groupIconIv.setImageResource(R.drawable.ic_swimming_pool_svgrepo_com);
                                break;
                            case "Walk":
                                groupIconIv.setImageResource(R.drawable.ic_running_shoes_8592);
                                break;
                            case "Art":
                                groupIconIv.setImageResource(R.drawable.ic_artist_color_palette_svgrepo_com);
                                break;
                            case "Party":
                                groupIconIv.setImageResource(R.drawable.ic_party_blower_birthday_svgrepo_com);
                                break;
                        }
                    } else {
                        Log.d("GET_ALL_EVENT_DATA_JOIN_BOTTOM_SHEET", Objects.requireNonNull(task.getException()).toString());
                    }
                });
    }

    private void setJoinBtnType(boolean isInGroup, boolean canBeJoined) {
        if (isInGroup) {
            joinEventBtn.setText("Enter");
        } else if (canBeJoined){
            joinEventBtn.setText("Join");
        } else {
            joinEventBtn.setText("can't");
        }
    }
}