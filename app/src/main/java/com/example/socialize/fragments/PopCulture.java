package com.example.socialize.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialize.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopCulture#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopCulture extends Fragment {


    public PopCulture() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_culture, container, false);
    }
}