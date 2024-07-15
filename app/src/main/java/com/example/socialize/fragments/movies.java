package com.example.socialize.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.socialize.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link movies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class movies extends Fragment {


    public movies() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_movies, container, false);
        RecyclerView recyclerView=v.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Baki");
        arrayList.add("Jack");
        arrayList.add("goku");
       // fragmentRecyclerAdapter adapter=new fragmentRecyclerAdapter(getContext(),arrayList);
       // recyclerView.setAdapter(adapter);
        return v;
    }
}