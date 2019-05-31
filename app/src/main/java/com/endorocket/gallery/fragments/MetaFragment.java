package com.endorocket.gallery.fragments;

import android.media.ExifInterface;
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

public class MetaFragment extends Fragment {

    private static final String TAG = "MetaFragment";
    private static final String PICTURE_FILE = "pictureFile";

    // widgets
    private TextView mTextView;

    // vars
    private File mPictureFile;

    public static MetaFragment getInstance(File pictureFile) {
        Log.d(TAG, "getInstance: starting creating MetaFragment with: " + pictureFile);

        MetaFragment fragment = new MetaFragment();

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

        View view = inflater.inflate(R.layout.fragment_meta, container, false);

        mTextView = view.findViewById(R.id.text_meta);

        Bundle bundle = getArguments();
        mPictureFile = (File) bundle.getSerializable(PICTURE_FILE);

        mTextView.setText(readExif(mPictureFile.getAbsolutePath()));

        return view;
    }

    String readExif(String file) {
        String exif = "Exif: " + file;
        try {
            ExifInterface exifInterface = new ExifInterface(file);

            exif += "\nGPS related:";
            exif += "\n TAG_GPS_DATESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
            exif += "\n TAG_GPS_TIMESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
            exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            exif += "\n TAG_GPS_LONGITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            exif += "\n TAG_GPS_PROCESSING_METHOD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return exif;
    }
}
