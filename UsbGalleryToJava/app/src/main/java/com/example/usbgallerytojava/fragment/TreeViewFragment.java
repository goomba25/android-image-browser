package com.example.usbgallerytojava.fragment;

import android.os.Bundle;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usbgallerytojava.R;
import com.example.usbgallerytojava.TreeViewAdapter;
import com.example.usbgallerytojava.popup.StorageListPopup;
import com.example.usbgallerytojava.service.DataModel;

import java.util.List;

public class TreeViewFragment extends Fragment {

    private static final String TAG = "Goomba#";
    private FragmentHost mFragmentHost;

    // Toolbar
    private ImageView mBackButton;
    private TextView mTitle;
    private ImageView mUsbConnectionButton;

    private StorageListPopup mStorageListPopup;

    private TextView mNoConnectionText;

    // RecyclerView
    private RecyclerView mTreeView;
    private TreeViewAdapter mAdapter;

    private final DataModel mDataModel;

    private final StorageListPopup.PopupCallback mPopupCallback =
            new StorageListPopup.PopupCallback() {
                @Override
                public void onItemClicked(String description) {
                    Log.d(TAG, "onItemClicked: " + description);
                    mFragmentHost.setSelectedStorage(description);
                    updateTreeView();
                }
            };

    private final DataModel.UpdateCallback mUpdateCallback =
            new DataModel.UpdateCallback() {
                @Override
                public void update() {
                    updateTreeView();
                }
            };

    public TreeViewFragment(DataModel dataModel) {
        mDataModel = dataModel;
        mDataModel.setUpdateCallback(mUpdateCallback);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentHost = (FragmentHost) requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tree_fragment, container, false);

        // Toolbar
        mBackButton = view.findViewById(R.id.back_button);
        mTitle = view.findViewById(R.id.title);
        mUsbConnectionButton = view.findViewById(R.id.usb_connection_button);

        // RecyclerView
        mTreeView = view.findViewById(R.id.tree_view);
        mNoConnectionText = view.findViewById(R.id.none_connection_text);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle.setText(R.string.tree_view_default_title_text);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentHost.goBack();
            }
        });

        mStorageListPopup = new StorageListPopup(getContext());
        mUsbConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStorageListPopup.showPopup(view);
            }
        });

        mAdapter = new TreeViewAdapter(getContext());
        mTreeView.setLayoutManager(mAdapter.getGridLayoutManager());
    }

    public void setConnection(boolean connection) {
        if ( connection ) {
            mNoConnectionText.setVisibility(View.INVISIBLE);
            mTreeView.setVisibility(View.VISIBLE);
        }
        else {
            mNoConnectionText.setVisibility(View.VISIBLE);
            mTreeView.setVisibility(View.INVISIBLE);
        }
        updatePopup();
    }

    public void updateTreeView() {
        mAdapter.update(mDataModel.getDirectoryList(), mDataModel.getFileList());
    }

    private void updatePopup() {
        List<StorageVolume> storageVolumes = mFragmentHost.getStorageList();
        mStorageListPopup.updateList(storageVolumes);
        mStorageListPopup.setPopupCallback(mPopupCallback);

        if ( mFragmentHost.getSelectedStorage() != null ) {
            mStorageListPopup.setSelectedItem(mFragmentHost.getSelectedStorage().getDescription(getContext()));
        }
    }
}
