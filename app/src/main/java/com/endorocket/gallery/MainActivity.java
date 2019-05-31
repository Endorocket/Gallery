package com.endorocket.gallery;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.endorocket.gallery.fragments.FoldersFragment;
import com.endorocket.gallery.fragments.GalleryGridFragment;
import com.endorocket.gallery.fragments.GalleryListFragment;
import com.endorocket.gallery.fragments.ImageDetailsFragment;
import com.endorocket.gallery.fragments.ItemGalleryFragment;
import com.endorocket.gallery.fragments.ViewPagerGalleryFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private static final String TAG = "MainActivity";
    private static int REQUEST_READWRITE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreate: savedInstanceState is null");

            Toolbar actionBarToolBar = findViewById(R.id.toolbar);
            setSupportActionBar(actionBarToolBar);

            checkPermissions();
        } else {
            Log.d(TAG, "onCreate: savedInstanceState is NOT null");
        }
    }


    private void program() {

        final SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        final String pictureChoice = sharedPreferences.getString(getString(R.string.images_folder), getString(R.string.default_choice));
        final String galleryView = sharedPreferences.getString(getString(R.string.gallery_view), getString(R.string.grid));

        if (pictureChoice == null || pictureChoice.equals(getString(R.string.default_choice))) {
            FoldersFragment foldersFragment = new FoldersFragment();
            doFragmentTransaction(foldersFragment, getString(R.string.fragment_folders), false, null);
        } else if (pictureChoice.equals("camera")) {

            File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            final File picsDir = new File(dcimDir, "Camera");
            final Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.pics_dir), picsDir);

            if (galleryView != null && galleryView.equals(getString(R.string.list))) {
                GalleryListFragment listFragment = new GalleryListFragment();
                doFragmentTransaction(listFragment, getString(R.string.fragment_list_gallery), false, bundle);
            } else {
                GalleryGridFragment gridFragment = new GalleryGridFragment();
                doFragmentTransaction(gridFragment, getString(R.string.fragment_grid_gallery), false, bundle);
            }
        } else {

            File picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            final Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.pics_dir), picsDir);

            if (galleryView != null && galleryView.equals(getString(R.string.list))) {
                GalleryListFragment listFragment = new GalleryListFragment();
                doFragmentTransaction(listFragment, getString(R.string.fragment_list_gallery), false, bundle);
            } else {
                GalleryGridFragment gridFragment = new GalleryGridFragment();
                doFragmentTransaction(gridFragment, getString(R.string.fragment_grid_gallery), false, bundle);
            }
        }

    }

    public void doFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        transaction.replace(R.id.main_container, fragment, tag);

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    @Override
    public void inflateFragment(String fragmentTag, Bundle bundle) {

        if (fragmentTag.equals(getString(R.string.fragment_folders))) {
            FoldersFragment foldersFragment = new FoldersFragment();
            doFragmentTransaction(foldersFragment, fragmentTag, true, bundle);

        } else if (fragmentTag.equals(getString(R.string.fragment_list_gallery))) {
            GalleryListFragment galleryListFragment = new GalleryListFragment();
            doFragmentTransaction(galleryListFragment, fragmentTag, true, bundle);

        } else if (fragmentTag.equals(getString(R.string.fragment_grid_gallery))) {
            GalleryGridFragment galleryGridFragment = new GalleryGridFragment();
            doFragmentTransaction(galleryGridFragment, fragmentTag, true, bundle);

        } else if (fragmentTag.equals(getString(R.string.fragment_gallery_item))) {
            ItemGalleryFragment itemGalleryFragment = new ItemGalleryFragment();
            doFragmentTransaction(itemGalleryFragment, fragmentTag, true, bundle);

        } else if (fragmentTag.equals(getString(R.string.fragment_image_details))) {
            ImageDetailsFragment imageDetailsFragment = new ImageDetailsFragment();
            doFragmentTransaction(imageDetailsFragment, fragmentTag, true, bundle);

        } else if (fragmentTag.equals(getString(R.string.fragment_viewpager_gallery))) {
            ViewPagerGalleryFragment viewPagerGalleryFragment = new ViewPagerGalleryFragment();
            doFragmentTransaction(viewPagerGalleryFragment, fragmentTag, true, bundle);

        }
    }

    private void checkPermissions() {

        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READWRITE_STORAGE);
        } else {
            program();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READWRITE_STORAGE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                program();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        Log.d(TAG, "onBackPressed: " + fragment.getTag());
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

}
