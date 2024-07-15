package com.example.socialize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.viewHolder>{
    Context context;
    ArrayList<String> arrayList;
    public chatAdapter(Context context, ArrayList<String>arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }
    public void setUpdatedList(ArrayList<String> filterArray){
        this.arrayList=filterArray;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.chatitem,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.name.setText(arrayList.get(position));
        holder.about.setText(arrayList.get(position));
        holder.date.setText(arrayList.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(context,holder.name.getText(),3000).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView name,about,date;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.chat_info);
            name=itemView.findViewById(R.id.name);
            about=itemView.findViewById(R.id.about);
            date=itemView.findViewById(R.id.date);
        }
    }
}
