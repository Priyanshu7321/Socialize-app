package com.example.socialize;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

import timber.log.Timber;

public class postContent extends AppCompatActivity {
    EditText postquestion;
    ImageView imageView;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        postquestion=findViewById(R.id.postQuestionId);
        imageView=findViewById(R.id.postImage);
        findViewById(R.id.sharePostButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                try {
                    sharePost();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        findViewById(R.id.addImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileSelector();
            }
        });
        findViewById(R.id.imageBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(postContent.this,chatList.class));
                finish();
            }
        });
        findViewById(R.id.writeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postquestion.requestFocus();
            }
        });

    }
    ActivityResultLauncher<Intent> activityResultLauncherForActivity=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
        if(result.getResultCode() == AppCompatActivity.RESULT_OK){
            uri=result.getData().getData();
            Timber.tag("uri from Activity").d(uri.toString());
            Picasso.get().load(uri).into(imageView);
        }
    });

    private void sharePost() throws NoSuchAlgorithmException {
        if(!postquestion.getText().toString().equals("") || imageView.getDrawable()!=null) {
            if(imageView.getDrawable()!=null){
                String imageName=generateUniqueHash();
                FirebaseStorage.getInstance().getReference()
                        .child(Objects.requireNonNull(getSharedPreferences("signup", MODE_PRIVATE).getString("UserVal", null)))
                        .child(imageName)
                        .putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri urlI=uri;
                                Timber.tag("url").d(urlI.toString());
                                FirebaseDatabase.getInstance().getReference().child("chats").child(Objects.requireNonNull(getSharedPreferences("signup", MODE_PRIVATE).getString("UserVal", null))).child("Post").child("Image").push().setValue(urlI.toString());
                                findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                                postquestion.setText("");
                                imageView.setImageBitmap(null);
                            }
                        });
                    }
                });

            }else{
                FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                        .child(Objects.requireNonNull(getSharedPreferences("signup", MODE_PRIVATE).getString("UserVal", null)))
                        .child("Post")
                        .child("Image")
                        .push()
                        .setValue("null");
            }
            FirebaseDatabase.getInstance().getReference().child("chats").child(Objects.requireNonNull(getSharedPreferences("signup", MODE_PRIVATE).getString("UserVal", null))).child("Post").child("Question").push().setValue(postquestion.getText().toString());

        }else{
            Toast.makeText(postContent.this,"Add something",3000).show();
        }
    }


    private void openFileSelector() {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activityResultLauncherForActivity.launch(intent);
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