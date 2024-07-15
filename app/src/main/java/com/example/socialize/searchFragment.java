package com.example.socialize;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class searchFragment extends Fragment {


    public searchFragment() {
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
        View v=inflater.inflate(R.layout.fragment_search, container, false);
        arrayList=new ArrayList<>();
        recyclerView =v.findViewById(R.id.fsRecyclerView);
        context=getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter=new userClassListAdapter(context,arrayList);
        recyclerView.setAdapter(adapter);
        searchView=v.findViewById(R.id.searchView);
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

        FirebaseDatabase.getInstance().getReference().child("chats").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                    userClass obj = new userClass();
                    Map<String, String> m = (Map<String, String>) snapshot1.getValue();
                    nameList.add(m.get("Name"));

                    if (Objects.equals(snapshot1.getKey(), context.getSharedPreferences("signup", Context.MODE_PRIVATE).getString("UserVal", ""))) {
                        continue;
                    }
                    Log.d("snapshotKey", snapshot1.getKey());
                    FirebaseStorage.getInstance().getReference().child(snapshot1.getKey()).child("profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            obj.name = m.get("Name");
                            obj.user = snapshot1.getKey();
                            obj.url = uri;
                            arrayList.add(obj);
                            adapter.notifyDataSetChanged();
                            Log.d("uri", String.valueOf(uri));
                        }
                    });

                }
                Log.d("obj.name", String.valueOf(urlList.size()));
            }
        });
    }
}