package com.example.usbgallerytojava;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.usbgallerytojava.fragment.FragmentHost;
import com.example.usbgallerytojava.fragment.TreeViewFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity
        implements FragmentHost, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "Goomba#";

    private StorageManager mStorageManager;
    private List<StorageVolume> mUsbStorages = new ArrayList<>();
    private StorageVolume mSelectedStorage = null;
    private TreeViewFragment mDefaultFragment = null;

    private final ContentObserver mContentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, @Nullable Uri uri) {
            super.onChange(selfChange, uri);

            String uriToString = uri.toString();
            if( uriToString.contains("content://media/external") || uriToString.contains("content://media/internal") ) {
                // do nothing
            }
            else {
                Log.d(TAG, "Content has changed for URI " + uri);
                updateStorages();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        mStorageManager = getSystemService(StorageManager.class);
        mDefaultFragment = new TreeViewFragment();
        launchFragment(mDefaultFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateStorages();
        getContentResolver().registerContentObserver(MediaStore.AUTHORITY_URI, true, mContentObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getContentResolver().unregisterContentObserver(mContentObserver);
    }

    @Override
    public void launchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment,
                        Integer.toString(getSupportFragmentManager().getBackStackEntryCount()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public List<StorageVolume> getStorageList() {
        return mUsbStorages;
    }

    @Override
    public void setSelectedStorage() {

    }

    @Override
    public StorageVolume getSelectedStorage() {
        return mSelectedStorage;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
        int stackCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "onBackPressed: ");

        if ( stackCount == 0 ) {
            finish();
        }
    }

    @Override
    public void onBackStackChanged() {
        Log.d(TAG, "onBackStackChanged: " + getSupportFragmentManager().getBackStackEntryCount());
    }

    private void updateStorages() {
        List<StorageVolume> volumes = mStorageManager.getRecentStorageVolumes();
        mUsbStorages.clear();
        for (StorageVolume volume : volumes) {
            if (volume.isPrimary()) {
                Log.d(TAG, "Primary Storage: " + volume.getDescription(this));
            } else if (volume.isEmulated()) {
                Log.d(TAG, "Emulated Storage: " + volume.getDescription(this));
            } else {
                Log.d(TAG, "USB Storage: " + volume.getDescription(this));
                mUsbStorages.add(volume);
            }
        }

        mDefaultFragment.setConnection(!mUsbStorages.isEmpty());
    }
}
