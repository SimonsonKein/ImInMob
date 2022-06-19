package com.simon.project.main_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.simon.project.R;
import com.simon.project.chat_fragments_activities.ChatActivity;
import com.simon.project.controllers.FirebaseEventController;
import com.simon.project.listeners.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainFindFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_find, container, false);
        init(view);
        clickListener(view);
        return view;
    }
    List<String> findUsersIDList;
    private void init(View view) {
        findUsersIDList = new ArrayList<>();
        recyclerViewOptions(view);
    }
    RecyclerView  recyclerView;
    private void recyclerViewOptions(View view) {
        recyclerView=  view.findViewById(R.id.find_users_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener()  {
                            @Override
                            public void onItemClick(View view, int position) {
                                // do whatever
                                Bundle bundle = new Bundle();
                                bundle.putString("id", findUsersIDList.get(position));
                                Navigation.findNavController(view).navigate(R.id.fragment_main_account, bundle);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                // do whatever
                            }
                        }));
    }

    private void clickListener(View view){
        ImageButton findUserBtn = view.findViewById(R.id.find_btn);
        findUserBtn.setOnClickListener(v -> {
            EditText nicknameET = view.findViewById(R.id.find_nickname_et);
            String findNickname = nicknameET.getText().toString();

            FirebaseEventController.setFindUsersListAdapter(requireContext(), requireActivity(), recyclerView, findNickname, findUsersIDList);
        });
    }
}