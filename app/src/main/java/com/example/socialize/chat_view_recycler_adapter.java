package com.example.socialize;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class chat_view_recycler_adapter extends RecyclerView.Adapter<chat_view_recycler_adapter.viewHolder>{
    Context context;
    ArrayList<chat_view_items_class> arrayList;
    public chat_view_recycler_adapter(Context context, ArrayList<chat_view_items_class> arrayList){
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.chat_view_item,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        if(Objects.equals(arrayList.get(position).condition, "0")){
            holder.itemView.setPadding(80,0,10,0);
            holder.linearLayout.setGravity(Gravity.END);
        }else{
            holder.itemView.setPadding(10,0,80,0);
            holder.linearLayout.setGravity(Gravity.START);
        }
        holder.itemText.setText(arrayList.get(position).msg);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        CardView cardChatView;
        TextView itemText;
        LinearLayout linearLayout;
        public viewHolder(View itemView){
            super(itemView);
            cardChatView=itemView.findViewById(R.id.cardChatView);
            itemText=itemView.findViewById(R.id.itemText);
            linearLayout=itemView.findViewById(R.id.linearLay);
        }
    }
}
