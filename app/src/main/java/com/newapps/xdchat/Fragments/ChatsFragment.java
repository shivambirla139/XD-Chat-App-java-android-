package com.newapps.xdchat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newapps.xdchat.Adapters.UsersAdapter;
import com.newapps.xdchat.Models.User;
import com.newapps.xdchat.R;
import com.newapps.xdchat.databinding.FragmentChatsBinding;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {

    FragmentChatsBinding binding;
    ArrayList<User> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth mAuth;


    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater,container,false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        UsersAdapter adapter = new UsersAdapter(getContext(),list);
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    user.setUserId(dataSnapshot.getKey());
                    if(!user.getUserId().equals(mAuth.getUid())) list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}