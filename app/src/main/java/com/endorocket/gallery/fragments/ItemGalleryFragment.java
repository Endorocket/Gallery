package com.endorocket.gallery.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.endorocket.gallery.IMainActivity;
import com.endorocket.gallery.R;
import com.ortiz.touchview.TouchImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class ItemGalleryFragment extends Fragment {

    private static final String TAG = "ItemGalleryFragment";
    private static final String PICTURE_FILE = "pictureFile";

    // widgets
    private TouchImageView mImageView;

    // vars
    private IMainActivity mIMainActivity;
    private File mPictureFile;

    public static ItemGalleryFragment getInstance(File pictureFile) {
        ItemGalleryFragment fragment = new ItemGalleryFragment();

        if (pictureFile != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(PICTURE_FILE, pictureFile);
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    public TouchImageView getImageView() {
        return mImageView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");

        View view = inflater.inflate(R.layout.fragment_gallery_item, container, false);
        setHasOptionsMenu(true);

        mImageView = view.findViewById(R.id.touch_image);

        getIncomingBundle();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: called");

        inflater.inflate(R.menu.menu_image, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.d(TAG, "onOptionsItemSelected: called " + id);

        switch (id) {
            case R.id.share:
                Toast.makeText(getContext(), "Nie udostępniono", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.set_as:
//                final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
                Toast.makeText(getContext(), "Tapety nie ustawiono", Toast.LENGTH_SHORT).show();
                break;

            case R.id.details:
                mIMainActivity.inflateFragment(getString(R.string.fragment_image_details), getArguments());
                return true;

            case R.id.edit:

                Uri uri = Uri.fromFile(mPictureFile);
                Bundle bundle = new Bundle();
                bundle.putParcelable("uri", uri);

                int color = getResources().getColor(R.color.colorPrimary);

                UCrop.Options options = new UCrop.Options();
                options.setToolbarColor(color);
                options.setMaxScaleMultiplier(1.000001f);

                UCrop.of(uri, uri)
                        .withOptions(options)
                        .useSourceImageAspectRatio()
                        .start(getContext(), this, UCrop.REQUEST_CROP);

                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private void getIncomingBundle() {

        Bundle bundle = getArguments();
        mPictureFile = (File) bundle.getSerializable(PICTURE_FILE);

        setImage();
    }

    private void setImage() {
        Log.d(TAG, "setImage: called");

        Glide.with(getContext())
                .asBitmap()
                .placeholder(R.color.colorPrimaryDark)
                .load(mPictureFile.getAbsolutePath())
                .into(mImageView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIMainActivity = (IMainActivity) getActivity();
    }

}
