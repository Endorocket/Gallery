package com.endorocket.gallery.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.endorocket.gallery.IMainActivity;
import com.endorocket.gallery.R;

import java.io.File;

public class SelectFolderDialog extends DialogFragment {

    private static final String TAG = "SelectFolderDialog";

    // widgets
    private Button mButtonCamera;
    private Button mButtonPictures;

    // vars
    private IMainActivity mIMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_folder_select, container, false);

        mButtonCamera = view.findViewById(R.id.button_camera);
        mButtonPictures = view.findViewById(R.id.button_pictures);

        final SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        final String galleryChoice = sharedPreferences.getString(getString(R.string.gallery_view), getString(R.string.grid));

        mButtonCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                final File picsDir = new File(dcimDir, "Camera");
                final Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.pics_dir), picsDir);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.images_folder), "camera");
                editor.apply();

                if (galleryChoice != null && galleryChoice.equals(getString(R.string.list))) {
                    mIMainActivity.inflateFragment(getString(R.string.fragment_list_gallery), bundle);
                } else {
                    mIMainActivity.inflateFragment(getString(R.string.fragment_grid_gallery), bundle);
                }
                getDialog().dismiss();
            }
        });

        mButtonPictures.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                final Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.pics_dir), picsDir);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.images_folder), "pictures");
                editor.apply();

                if (galleryChoice != null && galleryChoice.equals(getString(R.string.list))) {
                    mIMainActivity.inflateFragment(getString(R.string.fragment_list_gallery), bundle);
                } else {
                    mIMainActivity.inflateFragment(getString(R.string.fragment_grid_gallery), bundle);
                }
                getDialog().dismiss();
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
