package com.example.socialize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.socialize.fragments.fragmentRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class postingFragment extends Fragment {


    public postingFragment() {
        // Required empty public constructor
    }
    ViewPager2 viewPager;
    TabLayout tabLayout;
    ImageButton msg;
    TextView heading;
    List<postContentClass> arrayList;
    fragmentRecyclerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_posting, container, false);
        msg=v.findViewById(R.id.msg);
        heading=v.findViewById(R.id.heading);
        fragmentAdapter fragmentAdapter=new fragmentAdapter(this);
        heading.setText("Hello, "+getContext().getSharedPreferences("signup", Context.MODE_PRIVATE).getString("name","Sir") +"\nLet's explore your feed!");
        RecyclerView recyclerView=v.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList=new ArrayList<>();
        adapter=new fragmentRecyclerAdapter(getContext(),arrayList);
        recyclerView.setAdapter(adapter);
        fetchPost();
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), postContent.class);
                startActivity(intent);
            }
        });
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