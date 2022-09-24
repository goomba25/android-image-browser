package com.example.usbgallerytojava;

import android.view.View;

import com.example.usbgallerytojava.fragment.FragmentHost;

public class DirectoryTile {

    private String mName = null;
    private int mCount = 0;
    private View.OnClickListener mListener = null;

    public DirectoryTile(FragmentHost fragmentHost) {
    }

    public String getName() {
        return mName;
    }

    public int getCount() {
        return mCount;
    }

    public View.OnClickListener getOnClickListener() {
        return mListener;
    }
}
