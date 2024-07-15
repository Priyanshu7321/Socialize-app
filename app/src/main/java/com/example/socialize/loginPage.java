package com.example.socialize;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class loginPage extends AppCompatActivity {

    TextInputEditText email,password;
    MaterialButton loginButton;
    TextView signUpNav;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email=findViewById(R.id.loginEmail);
        password=findViewById(R.id.loginPassword);
        sharedPreferences=getSharedPreferences("signup",MODE_PRIVATE);
        loginButton=findViewById(R.id.loginButton);
        signUpNav=findViewById(R.id.signUPNav);
        if(sharedPreferences.getBoolean("logIN",false)){
            startActivity(new Intent(loginPage.this,chatList.class));
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFunc();
            }
        });
        signUpNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginPage.this, SignUpPage.class));
            }
        });
    }
    public void checkFunc(){
        String s1=email.getText().toString();
        String s2=password.getText().toString();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(!s1.isEmpty() && !s2.isEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(s1,s2).addOnCompleteListener(loginPage.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseDatabase.getInstance().getReference().child("chats").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                DataSnapshot snapshotf = null;
                                Map<String,String> m=null;
                                boolean userFound = false;
                                int count=0;
                                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    m= (Map<String, String>) snapshot.getValue();
                                    if(m!=null && Objects.equals(m.get("Email"), s1)){
                                        editor.putString("UserVal",snapshot.getKey());
                                        Log.d("USERVAL",snapshot.getKey());
                                        userFound=true;
                                        break;
                                    }
                                }

                                if (userFound) {
                                    if (m.containsKey("profile")) {
                                        Log.d("IMAGEURL", m.get("profile"));
                                        editor.putString("Url", m.get("profile"));
                                    } else {
                                        Log.d("IMAGEURL", "Profile key not found in the map.");
                                    }
                                    editor.putString("email", m.get("Email"));
                                    editor.putString("dob", m.get("Dob"));
                                    editor.putString("name", m.get("Name"));
                                    editor.putString("mobile", m.get("Mobile"));
                                    editor.putBoolean("logIN", true);
                                    editor.putBoolean("signUP", true);
                                    editor.apply();
                                } else {
                                    Log.d("IMAGEURL", "User not found.");
                                }
                                startActivity(new Intent(loginPage.this,chatList.class));
                            }
                        });
                    }
                }
            });
        }
    }

}