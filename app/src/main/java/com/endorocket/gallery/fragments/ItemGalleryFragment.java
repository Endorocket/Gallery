package com.endorocket.gallery.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.io.IOException;

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
        View view = inflater.inflate(R.layout.fragment_gallery_item, container, false);
        setHasOptionsMenu(true);

        mImageView = view.findViewById(R.id.touch_image);

        getIncomingBundle();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_image, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.share:
                Toast.makeText(getContext(), getString(R.string.no_functionality), Toast.LENGTH_SHORT).show();
                return true;

            case R.id.set_as:
//                final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
                Toast.makeText(getContext(), getString(R.string.no_functionality), Toast.LENGTH_SHORT).show();
                break;

            case R.id.details:
                mIMainActivity.inflateFragment(getString(R.string.fragment_image_details), getArguments());
                return true;

            case R.id.edit:

                Uri sourceUri = Uri.fromFile(mPictureFile);
                Uri destinationUri = createDestinationUri();

                int color = getResources().getColor(R.color.colorPrimary);

                UCrop.Options options = new UCrop.Options();
                options.setToolbarColor(color);
                options.setMaxScaleMultiplier(1.000001f);

                UCrop.of(sourceUri, destinationUri)
                        .withOptions(options)
                        .useSourceImageAspectRatio()
                        .start(getContext(), this, UCrop.REQUEST_CROP);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Uri createDestinationUri() {

        String parentPath = mPictureFile.getParent();
        String name = mPictureFile.getName();

        int indexOfDot = name.lastIndexOf(".");
        String extension = name.substring(indexOfDot);
        String postfix = "-crop";

        String newName = name.substring(0, indexOfDot) + postfix + extension;
        String destinationPathname = parentPath + "/" + newName;

        return Uri.fromFile(new File(destinationPathname));
    }

    private void getIncomingBundle() {

        Bundle bundle = getArguments();
        mPictureFile = (File) bundle.getSerializable(PICTURE_FILE);

        setImage();
    }

    private void setImage() {

        Glide.with(getContext())
                .asBitmap()
                .placeholder(R.color.colorPrimaryDark)
                .load(mPictureFile.getAbsolutePath())
                .into(mImageView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            
            File saveFile = new File(resultUri.getPath());
            boolean done = false;
            try {
                done = saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "onActivityResult: SAVED FILE: " + done + " " + saveFile.getPath());
            Toast.makeText(getContext(), "Cropped photo created", Toast.LENGTH_SHORT).show();

        } else if (resultCode == UCrop.RESULT_ERROR) {
            Log.e(TAG, "onActivityResult: NOPE");
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(getContext(), cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIMainActivity = (IMainActivity) getActivity();
    }

}
