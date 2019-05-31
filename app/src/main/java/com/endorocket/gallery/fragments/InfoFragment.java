package com.endorocket.gallery.fragments;

import android.support.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.endorocket.gallery.R;

import java.io.File;
import java.io.IOException;

public class InfoFragment extends Fragment {

    private static final String TAG = "InfoFragment";
    private static final String PICTURE_FILE = "pictureFile";

    // widgets
    private TextView mTextView;

    // vars
    private File mPictureFile;


    public static InfoFragment getInstance(File pictureFile) {
        Log.d(TAG, "getInstance: starting creating InfoFragment with: " + pictureFile);

        InfoFragment fragment = new InfoFragment();

        if (pictureFile != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(PICTURE_FILE, pictureFile);
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");

        View view = inflater.inflate(R.layout.fragment_info,container,false);

        mTextView = view.findViewById(R.id.text_info);

        Bundle bundle = getArguments();
        mPictureFile = (File) bundle.getSerializable(PICTURE_FILE);

        mTextView.setText(readExif(mPictureFile.getAbsolutePath()));

        return view;
    }

    String readExif(String file) {
        String exif = "Exif: " + file;
        try {
            ExifInterface exifInterface = new ExifInterface(file);

            exif += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            exif += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return exif;
    }

}
