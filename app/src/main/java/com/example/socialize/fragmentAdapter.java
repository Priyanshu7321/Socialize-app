package com.example.socialize;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.socialize.fragments.Games;
import com.example.socialize.fragments.PopCulture;
import com.example.socialize.fragments.Technology;
import com.example.socialize.fragments.movies;

public class fragmentAdapter extends FragmentStateAdapter {


    public fragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return new Games();
        }else if(position==1){
            return new movies();
        }else if(position==2){
            return new PopCulture();
        }else{
            return new Technology();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
