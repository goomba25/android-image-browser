package com.example.usbgallerytojava;

import android.net.Uri;
import android.view.View;

import com.example.usbgallerytojava.fragment.FragmentHost;

public class FileTile {
    private Uri mUri = null;
    private String mName = null;
    private View.OnClickListener mListener = null;

    public FileTile(FragmentHost fragmentHost, Uri uri, String imageName) {
        mUri = uri;
        mName = imageName;
    }

    public Uri getUri() {
        return mUri;
    }

    public String getName() {
        return mName;
    }

    public View.OnClickListener getOnClickListener() {
        return mListener;
    }
}
