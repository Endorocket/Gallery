package com.endorocket.gallery.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endorocket.gallery.IMainActivity;
import com.endorocket.gallery.IOnBackPressed;
import com.endorocket.gallery.R;
import com.endorocket.gallery.adapters.ExtendedViewPager;
import com.endorocket.gallery.adapters.GalleryPageAdapter;
import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.util.ArrayList;

public class ViewPagerGalleryFragment extends Fragment implements IOnBackPressed {

    private static final String TAG = "ViewPagerGalleryFragmen";

    // widgets
    private ExtendedViewPager mMyViewPager;

    // vars
    ArrayList<ItemGalleryFragment> mFragments;
    private IMainActivity mIMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");

        View view = inflater.inflate(R.layout.fragment_viewpager_gallery, container, false);

        mMyViewPager = view.findViewById(R.id.image_view_pager);

        init();

        return view;
    }

    private void init() {

        mFragments = new ArrayList<>();

        Bundle bundle = getArguments();
        File picsDir = (File) bundle.getSerializable(getString(R.string.pics_dir));
        int position = bundle.getInt(getString(R.string.position));

        File[] files = picsDir.listFiles();

        for (File file : files) {
            ItemGalleryFragment fragment = ItemGalleryFragment.getInstance(file);
            mFragments.add(fragment);
        }

        GalleryPageAdapter pagerAdapter = new GalleryPageAdapter(getChildFragmentManager(), mFragments);
        mMyViewPager.setAdapter(pagerAdapter);
        mMyViewPager.setCurrentItem(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIMainActivity = (IMainActivity) getActivity();
    }

    @Override
    public boolean onBackPressed() {
        int currentItem = mMyViewPager.getCurrentItem();
        ItemGalleryFragment fragment = mFragments.get(currentItem);
        TouchImageView imageView = fragment.getImageView();

        Log.d(TAG, "onBackPressed: fragment " + fragment);

        if (imageView == null) {
            Log.d(TAG, "onBackPressed: imageView is null");
            return false;
        }

        if (imageView.isZoomed()) {
            Log.d(TAG, "onBackPressed: returning true");
            imageView.resetZoom();
            return true;
        } else {
            Log.d(TAG, "onBackPressed: returning false");
            return false;
        }
    }
}
