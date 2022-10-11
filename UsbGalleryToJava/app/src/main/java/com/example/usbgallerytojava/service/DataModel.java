package com.example.usbgallerytojava.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.util.Log;

import com.example.usbgallerytojava.DirectoryTile;
import com.example.usbgallerytojava.FileTile;
import com.example.usbgallerytojava.fragment.FragmentHost;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DataModel {
    private static final String TAG = "Goomba#DataModel";

    private ContentResolver mResolver;
    private FragmentHost mFragmentHost;
    private List<DirectoryTile> mDirs = new ArrayList<>();
    private List<FileTile> mFiles = new ArrayList<>();
    private boolean mIsRoot;

    private String mRootPath;
    private String mCurrPath;
    private String mCurrDir;
    private Uri mRootContentUri;
    private HashMap<String, Long> mBucketMap = new HashMap<>();

    private UpdateCallback mCallback;
    public interface UpdateCallback {
        void update();
    }

    public void setUpdateCallback(UpdateCallback callback) {
        mCallback = callback;
    }

    public DataModel(Context context) {
        mResolver = context.getContentResolver();
        mFragmentHost = (FragmentHost) context;
    }

    public List<DirectoryTile> getDirectoryList() {
        return mDirs;
    }

    public List<FileTile> getFileList() {
        return mFiles;
    }

    public void enterRootDirectory(StorageVolume volume) {
        mIsRoot = true;

        File root = volume.getDirectory();
        Log.d(TAG, "enterRootDirectory: " + root.getName());
        String[] files = root.list();
        if (files != null) {
            List<String> list = Arrays.asList(files);
            Log.d(TAG, "enterRootDirectory: " + list);
        } else {
            Log.d(TAG, "enterRootDirectory: ");
        }
//        mRootPath = root.getAbsolutePath();
//        mCurrPath = mRootPath;
//        mRootContentUri = MediaStore.Images.Media.getContentUri(volume.getUuid().toLowerCase());
//        setBucketMap();
//        changeDirectory();
    }

    private void setBucketMap() {
        mBucketMap.clear();

        String[] projection = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.BUCKET_ID,
        };

        Log.d(TAG, "setBucketMap: " + mRootContentUri);
        Cursor cursor = mResolver.query(mRootContentUri, projection, null, null, null);

        if ( cursor != null ) {
            while (cursor.moveToNext()) {
                int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                int fileNameIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
                int bucketIdIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID);

                String data = cursor.getString(dataIndex);
                String fileName = cursor.getString(fileNameIndex);
                Long bucketId = cursor.getLong(bucketIdIndex);

                String bucketPath = data.replace(File.separator + fileName, "");
                mBucketMap.put(bucketPath, bucketId);
            }
            cursor.close();
        }

        Log.d(TAG, "setBucketMap - keys: " + mBucketMap.keySet());
    }

    private void changeDirectory() {
        mDirs.clear();
        mFiles.clear();

        // get Directory
        File root = new File(mCurrPath);
        File[] files = root.listFiles();
        if ( files != null ) {
            for (File file : files) {
                if ( file.isDirectory() && !file.isHidden() ) {
                    Uri uri = Uri.fromFile(file);
                    DirectoryTile tile = new DirectoryTile(mFragmentHost, uri, file.getName(), 0);
                    mDirs.add(tile);
                    Log.d(TAG, "Dir: " + file.getName());
                }
            }
        }

        // get File(image)
        String[] projection = {
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA,
        };
        String selection = MediaStore.Images.Media.BUCKET_ID + "=?";
        String[] selectionArgs;
        if ( mBucketMap.get(mCurrPath) == null ) {
            selectionArgs = new String[0];
        }
        else {
            selectionArgs = new String[] {Objects.requireNonNull(mBucketMap.get(mCurrPath)).toString()};
        }

        Cursor cursor = mResolver.query(mRootContentUri, projection, selection, selectionArgs, null);
        if ( cursor != null ) {
            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
                int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String name = cursor.getString(nameIndex);
                String data = cursor.getString(dataIndex);

                FileTile tile = new FileTile(mFragmentHost, Uri.parse(data), name);
                mFiles.add(tile);
                Log.d(TAG, "File: " + name);
            }
            cursor.close();
        }

        mCallback.update();
    }
}
