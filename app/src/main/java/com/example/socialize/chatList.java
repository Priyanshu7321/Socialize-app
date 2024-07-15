package com.example.socialize;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class chatList extends AppCompatActivity {


    SearchView searchView;
    FloatingActionButton flbt1;
    String st ;
    SmoothBottomBar bottomBar;
    ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseApp.initializeApp(this);

        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());
        bottomBar=findViewById(R.id.bottomBar);
        viewPager2=findViewById(R.id.viewPager2);
        chatList_viewPagerAdapter adapter=new chatList_viewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager2.setAdapter(adapter);
        findViewById(R.id.profileBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("signup",MODE_PRIVATE).edit().putBoolean("profilePage",false).apply();
                startActivity(new Intent(chatList.this, profilepage.class));
            }
        });
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int item) {
                if(item==0){
                    viewPager2.setCurrentItem(0);
                    return true;
                }else if(item==1){
                    viewPager2.setCurrentItem(1);
                    return true;
                }else if(item==2){
                    viewPager2.setCurrentItem(2);
                    return true;
                }else {
                    viewPager2.setCurrentItem(3);
                    return true;
                }
            }
        });
        findViewById(R.id.optionBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyPopup(v);
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomBar.setItemActiveIndex(0);
                        break;
                    case 1:
                        bottomBar.setItemActiveIndex(1);
                        break;
                    case 2:
                        bottomBar.setItemActiveIndex(2);
                        break;
                    default:
                        bottomBar.setItemActiveIndex(3);
                        break;
                }
            }
        });

    }
    public void applyPopup(View view){
        PopupMenu menu=new PopupMenu(chatList.this,view);
        menu.getMenuInflater().inflate(R.menu.popup_menuitems, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.setting){
                    Toast.makeText(chatList.this,"setting is clicked",3000).show();
                    return true;
                }else{
                    SharedPreferences.Editor editor=getSharedPreferences("signup",MODE_PRIVATE).edit();
                    editor.putBoolean("profilePage",false);
                    editor.putBoolean("signUP",false);
                    editor.putBoolean("logIN",false);
                    editor.putString("email","");
                    editor.putString("dob","");
                    editor.putString("name","");
                    editor.putString("mobile","");
                    editor.putString("UserVal","");
                    editor.putString("Url","");
                    editor.putBoolean("firstTime",true);
                    editor.apply();
                    return true;
                }
            }
        });
        menu.show();
    }

}