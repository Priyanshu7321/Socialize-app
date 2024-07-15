package com.example.socialize;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.LongToIntFunction;

public class chat_view extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<chat_view_items_class> arrayList;
    EditText chat_view_editext;
    ImageButton sendButton;

    FirebaseDatabase database;
    chat_view_recycler_adapter chatViewRecyclerAdapter;
    String origin,dest;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_view);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());

            // Combine system bars and IME insets
            Insets combinedInsets = Insets.of(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    Math.max(systemBars.bottom, imeInsets.bottom)
            );

            v.setPadding(combinedInsets.left, combinedInsets.top, combinedInsets.right, combinedInsets.bottom);

            return insets;
        });

        MaterialCardView cardView=findViewById(R.id.materialCardView);



        recyclerView=findViewById(R.id.chatviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chat_view_editext=findViewById(R.id.chat_view_edittext);
        sendButton=findViewById(R.id.sendButton);
        arrayList=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        chatViewRecyclerAdapter=new chat_view_recycler_adapter(getApplicationContext(),arrayList);
        recyclerView.setAdapter(chatViewRecyclerAdapter);
        origin=getSharedPreferences("signup",MODE_PRIVATE).getString("UserVal",null);
        dest=getIntent().getStringExtra("UserMemberValue");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chat_view_editext.getText().toString().equals("")) {
                    msg_send_func(chat_view_editext.getText().toString());
                    fetchChatData();
                }
            }
        });
        fetchChatData();
        FirebaseDatabase.getInstance().getReference().child("chats").child(origin).child("members").child(dest).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fetchChatData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void fetchChatData() {
        database.getReference().child("chats").child(origin).child("members").child(dest).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    arrayList.clear();
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        DataSnapshot snapshot1 = snapshot.child("msg");
                        DataSnapshot snapshot2 = snapshot.child("condition");
                        Iterator<DataSnapshot> iterator1=snapshot1.getChildren().iterator();
                        Iterator<DataSnapshot> iterator2=snapshot2.getChildren().iterator();
                        while(iterator1.hasNext() || iterator2.hasNext()){
                            if (iterator1.hasNext() || iterator2.hasNext()) {
                                DataSnapshot name1Child = iterator1.next();
                                DataSnapshot name2Child = iterator2.next();
                                arrayList.add(new chat_view_items_class(name1Child.getValue(String.class), String.valueOf(name2Child.getValue(Integer.class))));
                            }
                        }

                        chatViewRecyclerAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Firebase", "No data available.");
                    }
                } else {
                    Log.w("Firebase", "Failed to read value.", task.getException());
                }
            }
        });
    }

    private void msg_send_func(String string) {
        chat_view_editext.setText("");
        //User
        database.getReference().child("chats").child(origin).child("members").child(dest).child("condition").push().setValue(0);
        database.getReference().child("chats").child(origin).child("members").child(dest).child("msg").push().setValue(string);
        //Friend
        database.getReference().child("chats").child(dest).child("members").child(origin).child("condition").push().setValue(1);
        database.getReference().child("chats").child(dest).child("members").child(origin).child("msg").push().setValue(string);
        fetchChatData();
    }
}