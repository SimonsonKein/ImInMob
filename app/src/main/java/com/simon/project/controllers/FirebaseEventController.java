package com.simon.project.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.simon.project.MainActivity;
import com.simon.project.R;
import com.simon.project.adapters.ChatMembersAdapter;
import com.simon.project.adapters.ChatsAdapter;
import com.simon.project.adapters.EventsAdapter;
import com.simon.project.adapters.FindUsersAdapter;
import com.simon.project.adapters.FriendsAdapter;
import com.simon.project.adapters.NotificationAdapter;
import com.simon.project.bottom_model_fragments.CreateEventBottomSheet;
import com.simon.project.chat_fragments_activities.ChatActivity;
import com.simon.project.chat_fragments_activities.GroupInfoActivity;
import com.simon.project.map_actions.AddMarker;
import com.simon.project.model.EventInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FirebaseEventController {
    public static void createEvent(Activity activity,
                                   Context context,
                                   HashMap<Integer, Object> createEventDataMap,
                                   String UID,
                                   GoogleMap googleMap,
                                   LatLng markerLocation,
                                   View rootView,
                                   CreateEventBottomSheet createEventBottomSheet) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", ((EditText) createEventDataMap.get(R.string.event_title)).getText().toString());
        map.put("description", ((EditText) createEventDataMap.get(R.string.event_description)).getText().toString());
        map.put("date", ((TextView) createEventDataMap.get(R.string.event_date)).getText().toString());
        map.put("time", ((TextView) createEventDataMap.get(R.string.event_time)).getText().toString());
        map.put("category", ((Spinner) createEventDataMap.get(R.string.event_category)).getSelectedItem().toString());
        map.put("amount_members", ((Spinner) createEventDataMap.get(R.string.event_amount_members)).getSelectedItem());
        map.put("current_amount_members", 1);
        GeoPoint eventMarkerPosition = new GeoPoint(markerLocation.latitude, markerLocation.longitude);
        map.put("position", eventMarkerPosition);

        UUID eventID = UUID.randomUUID();

        EventInfo eventInfo = new EventInfo(eventID.toString(), (String) map.get("title"));
        FirebaseFirestore.getInstance()
                .collection("Events")
                .document(eventID.toString())
                .set(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        createGroup(activity, context, UID, eventID.toString(), rootView, createEventBottomSheet, eventInfo);
                        String eventTitle = (String) (((EditText) createEventDataMap.get(R.string.event_title)).getText().toString());
                        new AddMarker(context, googleMap, markerLocation, eventTitle,
                                ((Spinner) createEventDataMap.get(R.string.event_category)).getSelectedItem().toString(),
                                eventID.toString());

                        Log.d("CREATING_FIRESTORE_EVENT_DATA", "Creating successful");
                        Toast.makeText(activity, "Creating succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("CREATING_FIRESTORE_EVENT_DATA", task.getException().toString());
                        Toast.makeText(activity, "Creating failed\n" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static void createGroup(Activity activity, Context context, String UID, String eventID, View rootView,
                                    CreateEventBottomSheet createEventBottomSheet, EventInfo eventInfo) {
        HashMap<String, String> usersEventsMap = new HashMap<>();
        usersEventsMap.put("user_id", UID);
        usersEventsMap.put("event_id", eventID);
        FirebaseFirestore.getInstance()
                .collection("UsersEvents")
                .document()
                .set(usersEventsMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Navigation.findNavController(rootView).navigate(R.id.fragment_main_chats);
                        createEventBottomSheet.dismiss();
                        Intent intent = new Intent(activity, ChatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("event_info", eventInfo);
                        activity.startActivity(intent);
                        Log.d("CREATING_FIRESTORE_EVENT_DATA", "Creating successful");
                        Toast.makeText(activity, "Creating successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("CREATING_FIRESTORE_EVENT_DATA", task.getException().toString());
                        Toast.makeText(activity, "Creating failed\n" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void setChatsListAdapter(Activity activity, Context context, String UID, RecyclerView recyclerView, List<String> eventsIdList) {
        FirebaseFirestore.getInstance().collection("UsersEvents")
                .whereEqualTo("user_id", UID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            eventsIdList.add((String) document.get("event_id"));
                            Log.d("TAG", document.getId() + " => " + document.getData());
                        }
                        ChatsAdapter chatsAdapter = new ChatsAdapter(context, activity, eventsIdList);
                        recyclerView.setAdapter(chatsAdapter);
                    }
                });
    }

    public static void setFriendsListAdapter(Activity activity, Context context, String UID, RecyclerView recyclerView, List<String> friendsIdList) {
        FirebaseFirestore.getInstance().collection("UsersFriends")
                .whereEqualTo("user_1", UID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            friendsIdList.add((String) document.get("user_2"));
                            Log.d("TAG", document.getId() + " => " + document.getData());
                        }
                        FriendsAdapter friendsAdapter = new FriendsAdapter(context, activity, friendsIdList);
                        recyclerView.setAdapter(friendsAdapter);
                    }
                });
    }

    public static void setEventListAdapter(Activity activity, Context context, String UID, RecyclerView recyclerView, List<String> eventsList) {
        FirebaseFirestore.getInstance().collection("UsersEvents")
                .whereEqualTo("user_id", UID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            eventsList.add((String) document.get("event_id"));
                            Log.d("TAG", document.getId() + " => " + document.getData());
                        }
                        EventsAdapter eventsAdapter = new EventsAdapter(context, activity, eventsList);
                        recyclerView.setAdapter(eventsAdapter);
                    }
                });
    }

    public static void setNotificationListAdapter(List<String> friendsRequestList, NotificationAdapter notificationAdapter) {
        FirebaseFirestore.getInstance()
                .collection("FriendsRequest")
                .addSnapshotListener((snapshots, error) -> {
                    boolean isDataChanged = false;
                    for (DocumentChange documentChange : snapshots.getDocumentChanges()) {
                        if (documentChange.getDocument().get("friend_request").equals(FirebaseAuth.getInstance().getUid())) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                friendsRequestList.add(documentChange.getDocument().getId());
                            } else if (documentChange.getType() == DocumentChange.Type.REMOVED) {
                                for (int i = 0; i < friendsRequestList.size(); i++) {
                                    if (friendsRequestList.get(i).equals(documentChange.getDocument().getId())) {
                                        friendsRequestList.remove(i);
                                    }
                                }
                            }
                            isDataChanged = true;
                        }
                    }
                    if (isDataChanged) {
                        notificationAdapter.notifyDataSetChanged();
                    }
                });
    }


    public static void setChatNameTVAndLastMessage(List<String> eventsIdList, TextView chatNameTV, TextView lastMessageTV, int position) {
        FirebaseFirestore.getInstance().collection("Events").document(eventsIdList.get(position))
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseFirestore.getInstance().collection("Events")
                        .document(eventsIdList.get(position))
                        .collection("Messages")
                        .orderBy("date_time")
                        .limitToLast(1)
                        .get()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                chatNameTV.setText(Objects.requireNonNull(task.getResult().get("title")).toString());
                                for (DocumentSnapshot documentSnapshot : task1.getResult())
                                lastMessageTV.setText(Objects.requireNonNull(documentSnapshot.get("message")).toString());
                            }
                        });

                Log.d("SET_GROUP_NAME", "Set group name successful" + task.getResult().get("title"));
            } else {
                Log.d("SET_GROUP_NAME", Objects.requireNonNull(task.getException()).toString());
            }
        });
    }


    public static void setChatNameTV(List<String> eventsIdList, TextView chatNameTV, int position) {
        FirebaseFirestore.getInstance().collection("Events").document(eventsIdList.get(position))
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatNameTV.setText(Objects.requireNonNull(task.getResult().get("title")).toString());
                Log.d("SET_GROUP_NAME", "Set group name successful" + task.getResult().get("title"));
            } else {
                Log.d("SET_GROUP_NAME", Objects.requireNonNull(task.getException()).toString());
            }
        });
    }

    public static void setFriendRequestNameTV(List<String> friendsRequestList, TextView requestNicknameTV, int position) {
        FirebaseFirestore.getInstance().collection("FriendsRequest")
                .document(friendsRequestList.get(position))
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseFirestore.getInstance().collection("Users")
                        .document((String) task.getResult().get("user_id"))
                        .get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        requestNicknameTV.setText((String) task1.getResult().get("nickname"));
                    }

                });
                Log.d("SET_FRIENDS_REQUEST_NAME", "Set friend request name successful" + task.getResult().get("user_id"));
            } else {
                Log.d("SET_FRIENDS_REQUEST_NAME", Objects.requireNonNull(task.getException()).toString());
            }
        });
    }


    public static void setFriendNameTV(List<String> friendsIdList, TextView friendNameTV, int position) {
        FirebaseFirestore.getInstance().collection("Users").document(friendsIdList.get(position))
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                friendNameTV.setText(Objects.requireNonNull(task.getResult().get("nickname")).toString());
                Log.d("SET_FRIEND_NAME", "Set friend name successful" + task.getResult().get("nickname"));
            } else {
                Log.d("SET_FRIEND_NAME", Objects.requireNonNull(task.getException()).toString());
            }
        });
    }

    public static void setUserInfoTV(Activity activity, Context context, String UID, TextView tv, String tv_type) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(UID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tv.setText(task.getResult().get(tv_type).toString());
                        Log.d("SET_ACCOUNT_INFO", "Set account info successful" + task.getResult().get(tv_type));
                    } else {
                        Log.d("SET_ACCOUNT_INFO", Objects.requireNonNull(task.getException()).toString());
                    }
                });
    }


    public static void getEventInfoById(String eventId, HashMap<String, Object> eventInfoMap) {
        FirebaseFirestore.getInstance().collection("Events")
                .document(eventId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventInfoMap.put("event_title", task.getResult().get("title"));
                    } else {
                        Log.d("GETTER_EVENT_INFO", Objects.requireNonNull(task.getException()).toString());
                    }
                });
    }


    public static void acceptFriend(List<String> friendsRequestList, int position, NotificationAdapter notificationAdapter) {
        FirebaseFirestore.getInstance().collection("FriendsRequest")
                .document(friendsRequestList.get(position)).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String friendID = (String) task.getResult().get("user_id");
                        String UID = FirebaseAuth.getInstance().getUid();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("user_1", UID);
                        map.put("user_2", friendID);
                        FirebaseFirestore.getInstance().collection("UsersFriends")
                                .document().set(map).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d("ADD_FRIEND", "Successfully 1");
                            } else {
                                Log.d("ADD_FRIEND", task1.getException().toString());
                            }
                        });
                        map = new HashMap<>();
                        map.put("user_1", friendID);
                        map.put("user_2", UID);
                        FirebaseFirestore.getInstance().collection("UsersFriends")
                                .document().set(map).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d("ADD_FRIEND", "Successfully 2");
                            } else {
                                Log.d("ADD_FRIEND", task1.getException().toString());
                            }
                        });
                        FirebaseFirestore.getInstance().collection("FriendsRequest")
                                .document(friendsRequestList.get(position)).delete()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d("REMOVE_REQUEST", "Successful");
                                    } else {
                                        Log.d("REMOVE_REQUEST", task1.getException().toString());
                                    }
                                });

                    }
                });
    }

    public static void declineRequest(List<String> friendsRequestList, int position, NotificationAdapter notificationAdapter) {
        FirebaseFirestore.getInstance().collection("FriendsRequest")
                .document(friendsRequestList.get(position)).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        friendsRequestList.remove(position);
//                        notificationAdapter.notifyDataSetChanged();
                        Log.d("DECLINE_REQUEST", "Successful");
                    } else {
                        Log.d("DECLINE_REQUEST", task.getException().toString());
                    }
                });

    }

    public static void removeFriend(String friendID, Button friendBtn) {
        FirebaseFirestore.getInstance().collection("UsersFriends")
                .whereEqualTo("user_1", friendID)
                .whereEqualTo("user_2", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            String documentID = documentSnapshot.getId();
                            FirebaseFirestore.getInstance().collection("UsersFriends")
                                    .document(documentID).delete()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            friendBtn.setText("add friend");
                                            friendBtn.setTag("add_friend");
                                            Log.d("DELETING_FRIEND", "Successful");
                                        } else {
                                            Log.d("DELETING_FRIEND", task1.getException().toString());
                                        }
                                    });
                            Log.d("FINDING_FRIEND", documentSnapshot.getId());
                        }
                    } else {
                        Log.d("FINDING_FRIEND", task.getException().toString());
                    }
                });

        FirebaseFirestore.getInstance().collection("UsersFriends")
                .whereEqualTo("user_1", FirebaseAuth.getInstance().getUid())
                .whereEqualTo("user_2", friendID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            String documentID = documentSnapshot.getId();
                            FirebaseFirestore.getInstance().collection("UsersFriends")
                                    .document(documentID).delete()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            friendBtn.setText("add friend");
                                            friendBtn.setTag("add_friend");
                                            Log.d("DELETING_FRIEND", "Successful");
                                        } else {
                                            Log.d("DELETING_FRIEND", task1.getException().toString());
                                        }
                                    });
                            Log.d("FINDING_FRIEND", documentSnapshot.getId());
                        }
                    } else {
                        Log.d("FINDING_FRIEND", task.getException().toString());
                    }

                });
    }

    public static void showAllMarkers(Activity activity, Context context, GoogleMap googleMap) {
        FirebaseFirestore.getInstance().collection("Events")
                .get()
                .addOnCompleteListener(task -> {
                    String eventMarkerTitle;
                    String eventMarkerId;
                    String eventCategory;
                    GeoPoint eventPosition;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            eventMarkerId = documentSnapshot.getId();
                            eventMarkerTitle = (String) documentSnapshot.get("title");
                            eventPosition = documentSnapshot.getGeoPoint("position");
                            eventCategory = documentSnapshot.get("category").toString();
                            assert eventPosition != null;
                            LatLng markerLocation = new LatLng(eventPosition.getLatitude(), eventPosition.getLongitude());
                            new AddMarker(context, googleMap, markerLocation, eventMarkerTitle, eventCategory, eventMarkerId);
                        }
                    } else {
                        Log.d("SHOW_MARKERS", Objects.requireNonNull(task.getException()).toString());
                    }
                });
    }

    public static void setFindUsersListAdapter(Context context, Activity activity, RecyclerView recyclerView, String findNickname, List<String> findUsersIDList) {
        FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("nickname", findNickname).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        findUsersIDList.clear();
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            String usersId = documentSnapshot.getId();
                            findUsersIDList.add(usersId);
                            Log.d("FINDING_USER", "Successful");
                        }
                        FindUsersAdapter findUsersAdapter = new FindUsersAdapter(context, activity, findUsersIDList);
                        recyclerView.setAdapter(findUsersAdapter);
                    } else {
                        Log.d("FINDING_USER", task.getException().toString());
                    }
                });
    }
    
    public static void setUsersNicknameTVByID(String UID, TextView userNicknameTV) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(UID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userNicknameTV.setText((String) task.getResult().get("nickname"));
                        Log.d("SET_NICKNAME", "Successful"+task.getResult().get("nickname"));
                    } else {
                        Log.d("SET_NICKNAME", task.getException().toString());
                    }
                });
    }

    public static void setChatMemberListAdapter(Context context, String eventID, RecyclerView recyclerView) {
        List<String> UIDList = new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection("UsersEvents")
                .whereEqualTo("event_id", eventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            UIDList.add(documentSnapshot.get("user_id").toString());
                            Log.d("GET_MEMBER_LIST", "setChatMemberListAdapter: ");
                        }
                        ChatMembersAdapter chatMembersAdapter = new ChatMembersAdapter(context, UIDList);
                        recyclerView.setAdapter(chatMembersAdapter);
                    } else {
                        Log.d("GET_MEMBER_LIST", "setChatMemberListAdapter: ");
                    }
                });
    }
}