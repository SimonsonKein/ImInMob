package com.simon.project.main_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.simon.project.MainActivity;
import com.simon.project.R;
import com.simon.project.adapters.FriendsAdapter;
import com.simon.project.controllers.FirebaseEventController;
import com.simon.project.listeners.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFriendsFragment extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_main_friends, container, false);

        init(view);
        recyclerViewOptions();
        return view;
    }


    private RecyclerView recyclerView;
    private void init(View view) {
        recyclerView = view.findViewById(R.id.friends_recycler_view);
    }


    private void recyclerViewOptions() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<String> friendsIdList = new ArrayList<>();
        FirebaseEventController.setFriendsListAdapter(
                requireActivity(),
                requireContext(),
                FirebaseAuth.getInstance().getUid(),
                recyclerView,
                friendsIdList);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView,
                                            new RecyclerItemClickListener.OnItemClickListener()  {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        Bundle bundle = new Bundle();
                        bundle.putString("id", friendsIdList.get(position));
                        Navigation.findNavController(view).navigate(R.id.fragment_main_account, bundle);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                }));
    }


}