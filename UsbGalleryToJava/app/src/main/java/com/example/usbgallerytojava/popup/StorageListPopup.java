package com.example.usbgallerytojava.popup;

import android.content.Context;
import android.os.storage.StorageVolume;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.usbgallerytojava.R;

import java.util.ArrayList;
import java.util.List;

public class StorageListPopup {

    private static final int MAX_COUNT = 3;
    private static final int WIDTH = 500;
    private static final int DIVIDER_HEIGHT = 2;

    private static PopupWindow mPopupWindow;
    private Context mContext;
    private View mPopupView;

    private ListView mListView;
    private PopupAdapter mAdapter;

    private List<PopupItem> mEmptyItems = new ArrayList<>();
    private List<PopupItem> mAvailableItems = new ArrayList<>();
    private List<String> mStorageNameList = new ArrayList<>();

    private String mSelectedItem = null;

    private int mWidth = WIDTH;
    private int mHeight;
    private int mItemHeight = 80;
    private int mMaxHeight = (mItemHeight + DIVIDER_HEIGHT) * MAX_COUNT;
    private int mMinHeight = 80;

    private PopupCallback mCallback;
    public interface PopupCallback {
        void onItemClicked(String description);
    }

    public void setPopupCallback(PopupCallback callback) {
        mCallback = callback;
    }

    public StorageListPopup(Context context) {
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupView = inflater.inflate(R.layout.popup_view, null);

        mListView = mPopupView.findViewById(R.id.popup_list);
        mAdapter = new PopupAdapter(mContext);
        setEmptyList();
    }

    public void showPopup(View v) {
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PopupItem item = (PopupItem) adapterView.getItemAtPosition(i);
                mSelectedItem = item.getTitle();
                mCallback.onItemClicked(mSelectedItem);
                dismiss();
            }
        });

        mPopupWindow = new PopupWindow(
                mPopupView,
                WIDTH,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int l, int t, int r, int b, int ol, int ot, int or, int ob) {
                mPopupView.removeOnLayoutChangeListener(this);
                mHeight = b - t;
                if (mHeight > mMaxHeight) {
                    mHeight = mMaxHeight;
                    mPopupWindow.update(-1, mHeight);
                    mListView.getLayoutParams().height = mHeight;
                } else if (mHeight < mMinHeight) {
                    mHeight = mMinHeight;
                    mPopupWindow.update(-1, mHeight);
                    mListView.getLayoutParams().height = mHeight;
                }
            }
        });

        mPopupWindow.showAsDropDown(v, 20, 60, Gravity.BOTTOM | Gravity.END);
    }

    public void dismiss() {
        if ( mPopupWindow == null ) {
            return;
        }

        mPopupWindow.dismiss();
    }

    public void updateList(List<StorageVolume> volumes) {
        if ( !volumes.isEmpty() ) {
            mAvailableItems.clear();

            for ( StorageVolume volume : volumes ) {
                String name = volume.getDescription(mContext);
                PopupItem item = new PopupItem(name);
                mAvailableItems.add(item);
                mStorageNameList.add(name);
            }

            if ( mSelectedItem != null ) {
                if ( mStorageNameList.contains(mSelectedItem) ) {
                    int position = mStorageNameList.indexOf(mSelectedItem);

                    String temp = mStorageNameList.get(position);
                    mStorageNameList.remove(position);
                    mStorageNameList.add(0, temp);
                }
            }

            mAdapter.updateItem(mAvailableItems);
            mAdapter.setEnable(true);
            mAdapter.setSelectable(mSelectedItem != null);
        }
        else {
            mAdapter.updateItem(mEmptyItems);
            mAdapter.setEnable(false);
        }
    }

    public void setSelectedItem(String volumeName) {
        mSelectedItem = volumeName;
    }

    // List used when no devices
    private void setEmptyList() {
        PopupItem noConnectedDevice = new PopupItem(mContext.getString(R.string.popup_no_connected_device_text));
        mEmptyItems.add(noConnectedDevice);
    }
}
