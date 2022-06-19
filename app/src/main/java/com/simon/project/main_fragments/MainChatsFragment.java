package com.simon.project.main_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.simon.project.R;
import com.simon.project.chat_fragments_activities.ChatActivity;
import com.simon.project.controllers.FirebaseEventController;
import com.simon.project.listeners.RecyclerItemClickListener;
import com.simon.project.model.EventInfo;

import java.util.ArrayList;
import java.util.List;

public class MainChatsFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_chats, container, false);

        init(view);
        recyclerViewOptions();
        transitionFromChat();
        return view;
    }

    private void transitionFromChat() {
        TextView toolbarTV = requireActivity().findViewById(R.id.toolbar_tv);
        toolbarTV.setText(R.string.chats);
        requireActivity().findViewById(R.id.menu_bottom_navigation).setVisibility(View.VISIBLE);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
    }

    private RecyclerView recyclerView;
    private void init(View view) {
        recyclerView = view.findViewById(R.id.chats_recycler_view);
    }

    private void recyclerViewOptions() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<String> eventsIdList = new ArrayList<>();
        FirebaseEventController.setChatsListAdapter(
                requireActivity(),
                requireContext(),
                FirebaseAuth.getInstance().getUid(),
                recyclerView,
                eventsIdList);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener()  {
                            @Override
                            public void onItemClick(View view, int position) {
                                FirebaseFirestore.getInstance()
                                        .collection("Events")
                                        .document(eventsIdList.get(position))
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful())  {
                                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                EventInfo eventInfo = new EventInfo(
                                                        eventsIdList.get(position),
                                                        (String) task.getResult().get("title"));
                                                intent.putExtra("event_info", eventInfo);
                                                startActivity(intent);
                                            }
                                        });

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                // do whatever
                            }
                        }));
    }

}