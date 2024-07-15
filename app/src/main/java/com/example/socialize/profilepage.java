package com.example.socialize;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import android.Manifest;

import org.w3c.dom.Text;

import timber.log.Timber;

public class profilepage extends AppCompatActivity {

    EditText name,dob,mobile;
    TextView email;
    MaterialButton save;
    ImageButton penName,penEmail,penDOB,penMobile,imageButton1;
    ShapeableImageView imageButton;
    SharedPreferences sharedPreferences;
    String mail;
    public  int count=0;
    String imageUrl;
    Uri globalUri;
    String UserVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profilepage);
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
        sharedPreferences=getSharedPreferences("signup",MODE_PRIVATE);
        name=findViewById(R.id.profileName);
        email=findViewById(R.id.profileEmail);
        dob=findViewById(R.id.profileDOB);
        mobile=findViewById(R.id.profileMobile);
        save=findViewById(R.id.profileSaveBt);
        imageButton=findViewById(R.id.imageButton);
        imageButton1=findViewById(R.id.imageButton1);
        Intent intent1=getIntent();
        mail=sharedPreferences.getString("email",null);
        email.setText(mail);
        if(sharedPreferences.getBoolean("profilePage",false)){
            startActivity(new Intent(profilepage.this,chatList.class));
        }
        String url = sharedPreferences.getString("Url", null);
        if (url != null && !url.isEmpty()) {
            Timber.tag("urlImage").d(url);
            Picasso.get().load(url).into(imageButton);
        }
        if(!sharedPreferences.getBoolean("SelectImage",false)){
            int drawableResourceId = R.drawable.user;

             globalUri= Uri.parse("android.resource://" + getPackageName() + "/" + drawableResourceId);

        }
        UserVal=getSharedPreferences("signup",MODE_PRIVATE).getString("UserVal","");
        askPermit();
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    dob.requestFocus();
                    return true;
                }
                return false;
            }
        });

        dob.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    // Move focus to the next EditText
                    mobile.requestFocus();
                    return true;
                }
                return false;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString()!="" && email.getText().toString()!="" && dob.getText().toString()!="" && mobile.getText().toString()!=""){
                    Intent intent=new Intent(profilepage.this, chatList.class);

                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name",name.getText().toString());
                    editor.putString("dob",dob.getText().toString());
                    editor.putString("mobile",mobile.getText().toString());
                    editor.apply();
                    name.setText(sharedPreferences.getString("name",""));

                    dob.setText(sharedPreferences.getString("dob",""));

                    mobile.setText(sharedPreferences.getString("mobile",""));


                    firebaseSaving(name.getText().toString(),mail,dob.getText().toString(),mobile.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(profilepage.this,"fill all the details",3000).show();
                }
            }
        });
        findViewById(R.id.imageButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });


        name.setText(sharedPreferences.getString("name",""));
        dob.setText(sharedPreferences.getString("dob",""));
        mobile.setText(sharedPreferences.getString("mobile",""));

    }


    ActivityResultLauncher<String[]> activityResultLauncherPermission=registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),result->{
        if(result.containsValue(false)){
            askPermit();
        }
    });
    public void askPermit(){
        String[] permissions=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        activityResultLauncherPermission.launch(permissions);
    }
    ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
        if(result.getResultCode()==RESULT_OK && result.getData()!=null){
            getSharedPreferences("signup",MODE_PRIVATE).edit().putBoolean("ImageSelect",true);
            Picasso.get().load(result.getData().getData()).into(imageButton);
            globalUri=result.getData().getData();
        }
    });
    public void fileChooser(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        activityResultLauncher.launch(Intent.createChooser(intent,"Select picture"));
    }
    private void func(EditText edit) {
        edit.setFocusable(false);
        edit.setFocusableInTouchMode(false);
        edit.setClickable(false);
        edit.setCursorVisible(false);
    }
    String num;
    Integer it;
    public void firebaseSaving(String n,String e,String d,String mo){
        Log.d("android12",n+mo+e+d);


        SharedPreferences.Editor editor=getSharedPreferences("signup",MODE_PRIVATE).edit();
        FirebaseDatabase.getInstance().getReference().child("chats").child(UserVal).child("Name").setValue(n);
        FirebaseDatabase.getInstance().getReference().child("chats").child(UserVal).child("Dob").setValue(d);
        FirebaseDatabase.getInstance().getReference().child("chats").child(UserVal).child("Mobile").setValue(mo);
        FirebaseDatabase.getInstance().getReference().child("chats").child(UserVal).child("Email").setValue(e);
        editor.putBoolean("profilePage",true);
        saveImage(globalUri);
        editor.apply();


    }

    public void saveImage(Uri uir){
        SharedPreferences.Editor editor=sharedPreferences.edit();

        FirebaseStorage.getInstance().getReference().child(UserVal+"/profile.jpg").putFile(uir)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();
                                Picasso.get().load(uri.toString()).into(imageButton);
                                editor.putString("Url",imageUrl);
                                editor.apply();
                                FirebaseDatabase.getInstance().getReference().child("chats").child(UserVal).child("profile").setValue(imageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Timber.tag("profile").d("profile is attached");
                                    }
                                });
                                Log.d("FirebaseStorage", "Image URL: " + imageUrl);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FirebaseStorage", "Failed to get download URL", e);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle unsuccessful uploads
                        Log.e("FirebaseStorage", "Failed to upload image", e);
                    }
                });
    }
}