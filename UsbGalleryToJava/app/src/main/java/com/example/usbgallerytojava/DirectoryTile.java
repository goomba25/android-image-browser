package com.example.usbgallerytojava;

import android.net.Uri;
import android.view.View;

import com.example.usbgallerytojava.fragment.FragmentHost;

public class DirectoryTile {

    private Uri mUri = null;
    private String mName = null;
    private int mInternalCount = 0;
    private View.OnClickListener mListener = null;

    public DirectoryTile(FragmentHost fragmentHost, Uri uri, String dirName, int internalCount) {
        mUri = uri;
        mName = dirName;
        mInternalCount = internalCount;
    }

    public Uri getUri() {
        return mUri;
    }

    public String getDirName() {
        return mName;
    }

    public int getInternalCount() {
        return mInternalCount;
    }

    public View.OnClickListener getOnClickListener() {
        return mListener;
    }
}
