package com.example.socialize;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.xml.namespace.QName;


public class chatFragment extends Fragment {


    public chatFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    ArrayList<userClass> arrayList;
    userClassListAdapter adapter;
    EditText searchView;
    Context context;
    SharedPreferences sharedPreferences;
    List<Uri> urlList=new ArrayList<>();

    List<String> nameList=new ArrayList<>();
    URL url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_chat, container, false);
        arrayList=new ArrayList<>();
        recyclerView =v.findViewById(R.id.recyclerView);
        context=getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter=new userClassListAdapter(context,arrayList);
        recyclerView.setAdapter(adapter);
        searchView=v.findViewById(R.id.search);
        sharedPreferences=getContext().getSharedPreferences("signup",Context.MODE_PRIVATE);
        userRetrieval();
        //flbt1=findViewById(R.id.flbt1);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }
    public void filterList(String text){
        ArrayList<userClass> filterArray=new ArrayList<>();
        for(userClass item:arrayList){
            if(item.name.toLowerCase().contains(text.toLowerCase())){
                filterArray.add(item);
            }
        }
        if(filterArray.isEmpty()){
            Toast.makeText(context,"no item found",3000).show();
        }else{
            adapter.setUpdatedList(filterArray);
        }
    }
    public void userRetrieval(){

        FirebaseDatabase.getInstance().getReference().child("chats").child(Objects.requireNonNull(sharedPreferences.getString("UserVal", null))).child("members").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                        Log.d("snapshotKey",snapshot1.getKey());
                        FirebaseStorage.getInstance().getReference().child(snapshot1.getKey()).child("profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase.getInstance().getReference().child("chats").child(snapshot1.getKey()).child("Name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        userClass obj = new userClass();
                                        obj.name = (String) dataSnapshot.getValue();
                                        obj.url = uri;
                                        Log.d("obj.name",obj.name);
                                        arrayList.add(obj);
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                            }
                        }).addOnFailureListener(exception -> {
                            Log.e("FirebaseStorageError", "Error listing all items: ", exception);
                        });

                    }

                }
        });


    }

}