package com.simon.project.main_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.simon.project.MainActivity;
import com.simon.project.R;
import com.simon.project.StartActivity;
import com.simon.project.account_fragment_activity.NotificationActivity;
import com.simon.project.controllers.FirebaseEventController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainAccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_account, container, false);

        init(view);
        clickListener(view);
        statusListener(view);
        return view;
    }

    private void statusListener(View view) {
        String UID;
        if (getArguments() == null || getArguments().getString("id").equals(FirebaseAuth.getInstance().getUid()))  {
            UID = FirebaseAuth.getInstance().getUid();
        } else {
            UID = getArguments().getString("id");
        }
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(UID)
                .addSnapshotListener((snapshots, error) -> {
                    TextView statusTV = view.findViewById(R.id.account_status_tv);
                    statusTV.setText(snapshots.get("status").toString());
                });
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(UID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        TextView statusTV = view.findViewById(R.id.account_status_tv);
                        statusTV.setText(task.getResult().get("status").toString());
                    }
                });
    }

    Button friendBtn;
    private void addFriendBtnVisibility(View view) {
        friendBtn = view.findViewById(R.id.add_remove_friend_btn);
        if (getArguments() == null) {
            friendBtn.setVisibility(View.GONE);
        } else if (getArguments().getString("id").equals(FirebaseAuth.getInstance().getUid())) {
            friendBtn.setVisibility(View.GONE);
        } else {
            FirebaseFirestore.getInstance().collection("FriendsRequest")
                    .whereEqualTo("friend_request", getArguments().getString("id"))
                    .whereEqualTo("user_id", FirebaseAuth.getInstance().getUid())
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        FirebaseFirestore.getInstance().collection("UsersFriends")
                                .whereEqualTo("user_2", getArguments().getString("id"))
                                .get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                if (task1.getResult().isEmpty()) {
                                    friendBtn.setText("add friend");
                                    friendBtn.setTag("add_friend");
                                    Log.d("FINDING_FRIEND", "Is not a friend");
                                } else {
                                    friendBtn.setText("remove friend");
                                    friendBtn.setTag("remove_friend");
                                    Log.d("FINDING_FRIEND", "Successful");
                                }
                            } else {
                                Log.d("FINDING_FRIEND", task.getException().toString());
                            }
                        });
                    } else {
                        friendBtn.setText("remove request");
                        friendBtn.setTag("remove_request");
                    }
                } else {
                    Log.d("ADD_FRIEND_BTN", "addFriendBtnVisibility: " + Objects.requireNonNull(task.getException()).toString());
                }
            });
        }
    }

    private RecyclerView recyclerView;
    private List<HashMap<String, String>> eventsList = new ArrayList<>();
    private Button signOutBtn;
    ImageButton notificationBtn;
    String thisUID;
    private void init(View view) {
        notificationBtn = getActivity().findViewById(R.id.account_notification_img_btn);
        signOutBtn = view.findViewById(R.id.sign_out_btn);
        recyclerView = view.findViewById(R.id.event_list_rv);
        addFriendBtnVisibility(view);
        friendBtnListener(view);
        accountSettingsTV(view);
        checkFriendsRequest(notificationBtn);
        logoutVisibility(view);
        if (getArguments() == null) {
            thisUID = FirebaseAuth.getInstance().getUid();
        } else if (getArguments().getString("id").equals(FirebaseAuth.getInstance().getUid())) {
            thisUID = FirebaseAuth.getInstance().getUid();
        } else {
            thisUID = getArguments().getString("id");
        }

        ImageView imageView = view.findViewById(R.id.account_avatar_iv);
        switch (thisUID) {
            case "IKpoZKuWKjfAYx230EWjzZczbOx1"://Alexandr
                imageView.setImageResource(R.drawable.alex);
                break;
            case "npSAUa7r0wQQ6dtqRDYr862Q3Dw1":
                imageView.setImageResource(R.drawable.dima);
                break;
            case "DXYosr39b9ef8cWO4yeOa8ggVAB2":
                imageView.setImageResource(R.drawable.nastea);
                break;
            case "8L87L1WzzTS3P7TeenWsXMogv4V2":
                imageView.setImageResource(R.drawable.max);
                break;
            case "XFVZfLTOynhzNlQt5NsHZm4F2Zu1":
                imageView.setImageResource(R.drawable.kassa);
                break;
            case "ttJLzntZuEPZQCLvD3nFRSSdud73":
                imageView.setImageResource(R.drawable.dima);
                break;
            case "vAsPQb8vFge6H7PymILgrynB8Nb2":
                imageView.setImageResource(R.drawable.daria);
                break;
            case "w2ONKNTeFqRiEoGlkiCqgAtMeJS2":
                imageView.setImageResource(R.drawable.salagub);
                break;
        }
    }

    private void logoutVisibility(View view) {
        friendBtn = view.findViewById(R.id.add_remove_friend_btn);
        if (getArguments() == null) {
            friendBtn.setVisibility(View.GONE);
        } else if (getArguments().getString("id").equals(FirebaseAuth.getInstance().getUid())) {
            friendBtn.setVisibility(View.GONE);
        }
    }

    private void friendBtnListener(View view) {
        FirebaseFirestore.getInstance()
                .collection("FriendsRequest")
                .addSnapshotListener((snapshots, error) -> {
                    addFriendBtnVisibility(view);
                });
    }

    private void checkFriendsRequest(ImageButton notificationBtn) {
        FirebaseFirestore.getInstance()
                .collection("FriendsRequest")
                .addSnapshotListener((snapshots, error) -> {
                    List<DocumentChange> documentChangeList = new ArrayList<>();
                    for (DocumentChange documentChange : snapshots.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED)
                        if (documentChange.getDocument().get("friend_request").equals(FirebaseAuth.getInstance().getUid())) {
                            documentChangeList.add(documentChange);
                        }
                    }
                    if (documentChangeList.isEmpty()) {
                        notificationBtn.setImageResource(R.drawable.ic_baseline_notifications_none_24);
                    } else {
                        notificationBtn.setImageResource(R.drawable.ic_baseline_notifications_active_24);
                    }
                });
       /* FirebaseFirestore.getInstance().collection("FriendsRequest")
                .whereEqualTo("friend_request", FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                *//*if (task.getResult().isEmpty()) {
                    notificationBtn.setImageResource(R.drawable.ic_baseline_notifications_none_24);
                } else {
                    notificationBtn.setImageResource(R.drawable.ic_baseline_notifications_active_24);
                }*//*
            } else {
                Log.d("CHECK_FRIENDS_REQUEST", task.getException().toString());
            }
        });*/
    }

    private void recyclerViewOptions() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<String> eventsList = new ArrayList<>();
        FirebaseEventController.setEventListAdapter(
                requireActivity(),
                requireContext(),
                getAccountID(),
                recyclerView,
                eventsList);
    }

    private String getAccountID() {
        if (getArguments() == null) {
            return FirebaseAuth.getInstance().getUid();
        } else {
            return getArguments().getString("id");
        }
    }

    private void accountSettingsTV(View view) {
        recyclerViewOptions();
        TextView toolbar = getActivity().findViewById(R.id.toolbar_tv);
        toolbar.setText("MyAccount");
        TextView nicknameTV = view.findViewById(R.id.account_nickname_tv);
        FirebaseEventController.setUserInfoTV(
                requireActivity(),
                requireContext(),
                getAccountID(),
                nicknameTV,
                "nickname");
    }

    private void clickListener(View view) {
        notificationBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NotificationActivity.class));
        });

        signOutBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), StartActivity.class));
            getActivity().finish();
            logout();
        });

        friendBtn.setOnClickListener(v -> {
            switch (friendBtn.getTag().toString()) {
                case "remove_request":
                    assert getArguments() != null;
                    FirebaseFirestore.getInstance().collection("FriendsRequest")
                            .whereEqualTo("friend_request", getArguments().getString("id"))
                            .whereEqualTo("user_id", FirebaseAuth.getInstance().getUid())
                            .get().addOnCompleteListener(task -> {
                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                            FirebaseFirestore.getInstance().collection("FriendsRequest")
                                    .document(task.getResult().getDocuments().get(i).getId()).delete()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d("DELETING_DOCUMENT", "Successfully");
                                        } else {
                                            Log.d("DELETING_DOCUMENT", task1.getException().toString());
                                        }
                                    });
                        }
                        friendBtn.setText("add friend");
                        friendBtn.setTag("add_friend");
                    });
                    break;
                case "add_friend":
                    HashMap<String, String> map = new HashMap();
                    assert getArguments() != null;
                    map.put("user_id", FirebaseAuth.getInstance().getUid());
                    map.put("friend_request", getArguments().getString("id"));
                    FirebaseFirestore.getInstance().collection("FriendsRequest").add(map)
                            .addOnCompleteListener(task -> {
                                friendBtn.setText("remove request");
                                friendBtn.setTag("remove_request");
                            });
                    break;
                case "remove_friend":
                    FirebaseEventController.removeFriend(getArguments().getString("id"), friendBtn);
                    break;
            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}