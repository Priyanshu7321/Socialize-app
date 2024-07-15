package com.example.socialize.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.socialize.R;
import com.example.socialize.postContentClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Multiset;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Games extends Fragment {


    public Games() {
        // Required empty public constructor
    }
    List<postContentClass> arrayList;
    fragmentRecyclerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_games, container, false);
        RecyclerView recyclerView=v.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         arrayList=new ArrayList<>();
        adapter=new fragmentRecyclerAdapter(getContext(),arrayList);
        recyclerView.setAdapter(adapter);
        fetchPost();
        return v;
    }
    public void fetchPost(){
        arrayList.clear();
        FirebaseDatabase.getInstance().getReference().child("chats").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        Map<String,String> userInfo= (Map<String, String>) dataSnapshot1.getValue();
                        Map<String,String> questionList = (Map<String, String>) dataSnapshot1.child("Post").child("Question").getValue();
                        Map<String,String> imageList = (Map<String, String>) dataSnapshot1.child("Post").child("Image").getValue();

                        if(questionList!=null && !questionList.isEmpty()) {
                            List<Map.Entry<String, String>> list1 = new ArrayList<>(questionList.entrySet());
                            List<Map.Entry<String, String>> list2 = new ArrayList<>(imageList.entrySet());
                            for (int i = 0; i < imageList.size(); i++) {
                                arrayList.add(new postContentClass(userInfo.get("Name"), list1.get(i).getValue(), userInfo.get("profile"), list2.get(i).getValue()));
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}