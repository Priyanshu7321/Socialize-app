package com.example.socialize.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.socialize.R;
import com.example.socialize.postContentClass;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class fragmentRecyclerAdapter extends RecyclerView.Adapter<fragmentRecyclerAdapter.viewHolder> {
    Context context;
    List<postContentClass> arrayList;
    public fragmentRecyclerAdapter(Context context, List<postContentClass> arrayList){
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.fragment_item,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.creatorName.setText(arrayList.get(position).userName);
        holder.question.setText(arrayList.get(position).postQuestion);
        Picasso.get().load(arrayList.get(position).profileUri).into(holder.imageButton);
        Picasso.get().load(arrayList.get(position).contentUri).into(holder.postImage);
        Log.d("URIS",arrayList.get(position).contentUri.toString());
        Log.d("URIS",arrayList.get(position).profileUri.toString());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView imageButton;
        TextView creatorName,question;
        ShapeableImageView postImage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            creatorName=itemView.findViewById(R.id.creatorName);
            question=itemView.findViewById(R.id.question);
            postImage=itemView.findViewById(R.id.postImage);
            imageButton=itemView.findViewById(R.id.profilePost);
        }
    }
}
