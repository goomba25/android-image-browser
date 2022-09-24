package com.example.usbgallerytojava;

import android.view.View;

import com.example.usbgallerytojava.fragment.FragmentHost;

public class FileTile {
    private String mName = null;
    private View.OnClickListener mListener = null;

    public FileTile(FragmentHost fragmentHost) {
    }

    public String getName() {
        return mName;
    }

    public View.OnClickListener getOnClickListener() {
        return mListener;
    }
}
