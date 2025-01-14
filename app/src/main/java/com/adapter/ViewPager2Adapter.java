package com.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPager2Adapter extends FragmentStateAdapter {

    ArrayList<Fragment> listOfFragments;

    public ViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<Fragment> listOfFragments) {
        super(fragmentManager, lifecycle);
        this.listOfFragments = listOfFragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return listOfFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return listOfFragments.size();
    }
}