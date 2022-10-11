package com.example.usbgallerytojava;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.usbgallerytojava.fragment.FragmentHost;
import com.example.usbgallerytojava.fragment.TreeViewFragment;
import com.example.usbgallerytojava.service.DataModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity
        implements FragmentHost, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "Goomba#Activity";

    private StorageManager mStorageManager;
    private List<StorageVolume> mUsbStorages = new ArrayList<>();
    private StorageVolume mSelectedStorage = null;
    private TreeViewFragment mDefaultFragment = null;
    private DataModel mDataModel;

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

        getPermissions();

        mStorageManager = getSystemService(StorageManager.class);
        mDataModel = new DataModel(this);
        mDefaultFragment = new TreeViewFragment(mDataModel);
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
    public void setSelectedStorage(String selectedStorage) {
        if ( selectedStorage != null ) {
            for (StorageVolume volume : mUsbStorages) {
                if ( volume.getDescription(this).equals(selectedStorage) ) {
                    mSelectedStorage = volume;
                    mDataModel.enterRootDirectory(volume);
                    break;
                }
            }
        }
        else {
            mSelectedStorage = null;
        }
    }

    @Override
    public StorageVolume getSelectedStorage() {
        return mSelectedStorage;
    }

    @Override
    public void changeDirectory(String dirName) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            boolean check_result = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
        }
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

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            String[] permisstion = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permisstion, 1);
        }
    }
}
