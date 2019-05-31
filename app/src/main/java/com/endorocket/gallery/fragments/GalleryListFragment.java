package com.endorocket.gallery.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.endorocket.gallery.IMainActivity;
import com.endorocket.gallery.IOnBackPressed;
import com.endorocket.gallery.R;
import com.endorocket.gallery.adapters.RecyclerViewAdapter;
import com.otaliastudios.zoom.ZoomLayout;

import java.io.File;
import java.util.ArrayList;

public class GalleryListFragment extends Fragment implements RecyclerViewAdapter.OnImageListener, IOnBackPressed {

    private static final String TAG = "GalleryGridFragment";

    // widgets
    private ZoomLayout mZoomLayout;
    private RecyclerView mRecyclerView;

    // vars
    private ArrayList<String> mImagePaths;
    private ArrayList<File> mFiles;
    private IMainActivity mIMainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_gallery, container, false);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        mFiles = new ArrayList<>();
        mImagePaths = new ArrayList<>();

        mZoomLayout = view.findViewById(R.id.zoom_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        initImageBitmaps();
        initRecyclerView();

        mZoomLayout.setHasClickableChildren(true);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float width = dm.widthPixels / dm.xdpi;
        float height = dm.heightPixels / dm.ydpi;

        Log.d("debug", "Width inches : " + width);
        Log.d("debug", "Height inches : " + height);

        mZoomLayout.setMinZoom(height / 5, ZoomLayout.TYPE_REAL_ZOOM);
        mZoomLayout.setMaxZoom(height * 2 / 3, ZoomLayout.TYPE_REAL_ZOOM);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: called");

        inflater.inflate(R.menu.menu_list_gallery, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.d(TAG, "onOptionsItemSelected: called " + id);

        switch (id) {
            case R.id.to_grid:
                SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.gallery_view), getString(R.string.grid));
                editor.apply();

                mIMainActivity.inflateFragment(getString(R.string.fragment_grid_gallery), getArguments());
                break;

            case R.id.switch_folder:
                SelectFolderDialog dialog = new SelectFolderDialog();
                dialog.setTargetFragment(GalleryListFragment.this, 1);
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
            mImagePaths.add(file.getAbsolutePath());
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mImagePaths, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
