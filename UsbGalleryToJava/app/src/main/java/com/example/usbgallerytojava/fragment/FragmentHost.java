package com.example.usbgallerytojava.fragment;

import android.os.storage.StorageVolume;

import androidx.fragment.app.Fragment;

import java.util.List;

public interface FragmentHost {
    void launchFragment(Fragment fragment);
    void goBack();
    List<StorageVolume> getStorageList();
    void setSelectedStorage(String selectedStorage);
    StorageVolume getSelectedStorage();
    void changeDirectory(String dirName);
}
