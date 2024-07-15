package com.example.socialize;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class SignUpPage extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Boolean check;
    TextInputEditText email,password,cpassword;
    FirebaseAuth auth;
    MaterialButton materialButton;
    CheckBox checkBox1,checkBox2;
    String UserVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth= FirebaseAuth.getInstance();
        checkBox2=findViewById(R.id.checkBox2);
        sharedPreferences= getSharedPreferences("signup",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("signUP",false)){
            Intent in=new Intent(SignUpPage.this,chatList.class);
            startActivity(in);
        }
        findViewById(R.id.loginNav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpPage.this, loginPage.class));
            }
        });
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        cpassword=findViewById(R.id.cpassword);
        findViewById(R.id.signUpBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerEmail(email.getText().toString(),password.getText().toString());
            }
        });
        if(sharedPreferences.getBoolean("firstTime",true)) {
            try {
                UserVal = generateUniqueHash();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void registerEmail(String email,String password){
        if(!email.equals("") && !password.equals("") && !cpassword.equals("") && checkBox2.isChecked()){
            if(password.equals(cpassword.getText().toString())){
                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(SignUpPage.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(SignUpPage.this,"Account is created",3000).show();
                        Intent intent=new Intent(SignUpPage.this,profilepage.class);
                        intent.putExtra("mail",email);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("email",email);
                        editor.putBoolean("signUP",true);
                        editor.putString("UserVal",UserVal);
                        editor.putBoolean("firstTime",false);
                        editor.apply();
                        startActivity(intent);
                    }
                }).addOnFailureListener(SignUpPage.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpPage.this,"Try again",3000).show();
                    }
                });
            }
        }
    }
    public static String generateUniqueHash() throws NoSuchAlgorithmException {
        // Get the current timestamp
        long currentTimeMillis = System.currentTimeMillis();

        // Generate a random UUID
        String randomUUID = UUID.randomUUID().toString();

        // Combine the timestamp and the random UUID
        String input = currentTimeMillis + randomUUID;

        // Generate the hash using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());

        // Convert the hash bytes to a hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}