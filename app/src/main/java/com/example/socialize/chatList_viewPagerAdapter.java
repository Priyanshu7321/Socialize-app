package com.example.socialize;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class chatList_viewPagerAdapter extends FragmentStateAdapter {

    public chatList_viewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new postingFragment();
            case 1:
                return new chatFragment();
            case 2:
                return new callFragment();
            case 3:
                return new searchFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
