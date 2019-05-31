package com.endorocket.gallery.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.endorocket.gallery.IMainActivity;
import com.endorocket.gallery.R;

import java.io.File;

public class FoldersFragment extends Fragment {

    private static final String TAG = "FoldersFragment";

    // widgets
    private Button mButtonCameraGallery;
    private Button mButtonPictures;

    // vars
    private IMainActivity mIMainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders, container, false);

        mButtonCameraGallery = view.findViewById(R.id.btn_gallery);
        mButtonPictures = view.findViewById(R.id.btn_pictures);

        mButtonCameraGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                final File picsDir = new File(dcimDir, "Camera");

                final Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.pics_dir), picsDir);

                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                String galleryChoice = sharedPreferences.getString(getString(R.string.gallery_view), getString(R.string.grid));

                if (galleryChoice != null && galleryChoice.equals(getString(R.string.list))) {
                    mIMainActivity.inflateFragment(getString(R.string.fragment_list_gallery), bundle);
                } else {
                    mIMainActivity.inflateFragment(getString(R.string.fragment_grid_gallery), bundle);
                }
            }
        });

        mButtonPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                final Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.pics_dir), picsDir);

                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                String galleryChoice = sharedPreferences.getString(getString(R.string.gallery_view), getString(R.string.grid));

                if (galleryChoice != null && galleryChoice.equals(getString(R.string.list))) {
                    mIMainActivity.inflateFragment(getString(R.string.fragment_list_gallery), bundle);
                } else {
                    mIMainActivity.inflateFragment(getString(R.string.fragment_grid_gallery), bundle);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIMainActivity = (IMainActivity) getActivity();
    }

}
