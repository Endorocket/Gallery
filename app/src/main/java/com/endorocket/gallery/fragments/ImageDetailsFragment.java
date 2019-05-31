package com.endorocket.gallery.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.endorocket.gallery.R;
import com.endorocket.gallery.adapters.SectionsPageAdapter;

import java.io.File;

public class ImageDetailsFragment extends Fragment {

    private static final String TAG = "ImageDetailsFragment";

    // widgets
    private ViewPager mViewPager;

    // vars
    private File mPictureFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");

        View view = inflater.inflate(R.layout.fragment_image_details, container, false);
        setHasOptionsMenu(true);

        mPictureFile = (File) getArguments().getSerializable(getString(R.string.picture_file));

        mViewPager = view.findViewById(R.id.view_pager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.d(TAG, "setupViewPager: called");

        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());

        InfoFragment infoFragment = InfoFragment.getInstance(mPictureFile);
        adapter.addFragment(infoFragment, "INFO");

        MetaFragment metaFragment = MetaFragment.getInstance(mPictureFile);
        adapter.addFragment(metaFragment, "META");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}
