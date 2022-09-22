package com.example.usbgallerytojava.fragment;

import androidx.fragment.app.Fragment;

public interface FragmentHost {
    void launchFragment(Fragment fragment);
    void goBack();
}
