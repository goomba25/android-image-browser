package com.example.usbgallerytojava.fragment;

import android.os.Bundle;
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

public class TreeViewFragment extends Fragment {

    private FragmentHost mFragmentHost;

    // Toolbar
    private ImageView mBackButton;
    private TextView mTitle;
    private ImageView mUsbConnectionButton;

    private TextView mNoConnectionText;

    // RecyclerView
    private RecyclerView mTreeView;
    private TreeViewAdapter mAdapter;

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

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentHost.goBack();
            }
        });

        mTitle.setText(R.string.tree_view_default_title_text);

        mAdapter = new TreeViewAdapter(getContext());
        mTreeView.setLayoutManager(mAdapter.getGridLayoutManager());
    }
}
