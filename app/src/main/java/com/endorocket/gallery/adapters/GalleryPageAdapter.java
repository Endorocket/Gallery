package com.endorocket.gallery.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class GalleryPageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<? extends Fragment> mFragments;

    public GalleryPageAdapter(FragmentManager fm, ArrayList<? extends Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
