package com.endorocket.gallery.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.endorocket.gallery.IMainActivity;
import com.endorocket.gallery.IOnBackPressed;
import com.endorocket.gallery.R;
import com.endorocket.gallery.adapters.RecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;

public class GalleryGridFragment extends Fragment implements RecyclerViewAdapter.OnImageListener, IOnBackPressed {

    private static final String TAG = "GalleryListFragment";

    // widgets
    private RecyclerView mRecyclerView;

    // vars
    private ArrayList<String> mImages;
    private ArrayList<File> mFiles;
    private IMainActivity mIMainActivity;
    private GridLayoutManager mLayoutManager;
    private ScaleGestureDetector mScaleGestureDetector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");

        View view = inflater.inflate(R.layout.fragment_grid_gallery, container, false);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        mFiles = new ArrayList<>();
        mImages = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.recycler_view);

        initImageBitmaps();
        initRecyclerView();

        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {

                float currentSpan = detector.getCurrentSpan();
                float previousSpan = detector.getPreviousSpan();

                if (currentSpan > 300 && detector.getTimeDelta() > 300) {

                    if (currentSpan - previousSpan < -1) {
                        int currentGridColumns = mLayoutManager.getSpanCount();
                        if (currentGridColumns < 8) {
//                            TransitionManager.beginDelayedTransition(mRecyclerView);
                            mLayoutManager.setSpanCount(currentGridColumns + 1);
                        }
                        return true;

                    } else if (currentSpan - previousSpan > 1) {
                        int currentGridColumns = mLayoutManager.getSpanCount();
                        if (currentGridColumns > 2) {
//                            TransitionManager.beginDelayedTransition(mRecyclerView);
                            mLayoutManager.setSpanCount(currentGridColumns - 1);
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleGestureDetector.onTouchEvent(event);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: called");

        inflater.inflate(R.menu.menu_grid_gallery, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.d(TAG, "onOptionsItemSelected: called " + id);

        switch (id) {
            case R.id.to_list:
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.gallery_view), getString(R.string.list));
                editor.apply();

                mIMainActivity.inflateFragment(getString(R.string.fragment_list_gallery), getArguments());
                break;

            case R.id.switch_folder:
                SelectFolderDialog dialog = new SelectFolderDialog();
                dialog.setTargetFragment(GalleryGridFragment.this, 1);
                dialog.show(getFragmentManager(), getString(R.string.dialog_switch_folder));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        Bundle bundle = getArguments();
        File picsDir = (File) bundle.getSerializable(getString(R.string.pics_dir));

        File[] files = picsDir.listFiles();

        for (File file : files) {
            mFiles.add(file);
            mImages.add(file.getAbsolutePath());
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mImages, this);
        mRecyclerView.setAdapter(adapter);
        mLayoutManager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIMainActivity = (IMainActivity) getActivity();
    }

    @Override
    public void onImageClick(int position) {
        Log.d(TAG, "onImageClick: called");

        Bundle oldBundle = getArguments();
        File picsDir = (File) oldBundle.getSerializable(getString(R.string.pics_dir));

        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.pics_dir), picsDir);
        bundle.putInt(getString(R.string.position), position);

        mIMainActivity.inflateFragment(getString(R.string.fragment_viewpager_gallery), bundle);
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }
}
