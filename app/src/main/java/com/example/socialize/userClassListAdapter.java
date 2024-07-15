package com.example.socialize;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class userClassListAdapter extends RecyclerView.Adapter<userClassListAdapter.viewHolder> {
    Context context;
    List<userClass> userClassList;
    public userClassListAdapter(Context context, List<userClass> userClassList){
        this.context=context;
        this.userClassList=userClassList;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.chatitem,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.name.setText(userClassList.get(position).name);
        Picasso.get().load(String.valueOf(userClassList.get(position).url)).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, chat_view.class);
                intent.putExtra("UserMemberValue",userClassList.get(holder.getAdapterPosition()).user);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userClassList.size();
    }
    public void setUpdatedList(ArrayList<userClass> filterArray){
        this.userClassList=filterArray;
        notifyDataSetChanged();
    }
    public class viewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView imageView;
        TextView name,msg,date;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.userImage);
            name=itemView.findViewById(R.id.name);
            msg=itemView.findViewById(R.id.about);
            date=itemView.findViewById(R.id.date);
        }
    }
}
