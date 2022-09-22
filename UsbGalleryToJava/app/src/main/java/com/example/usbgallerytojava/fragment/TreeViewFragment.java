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

import com.example.usbgallerytojava.R;

public class TreeViewFragment extends Fragment {

    private FragmentHost mFragmentHost;
    private ImageView mBackButton;
    private TextView mTitle;
    private ImageView mUsbConnectionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentHost = (FragmentHost) requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tree_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBackButton = view.findViewById(R.id.back_button);
        mTitle = view.findViewById(R.id.title);
        mUsbConnectionButton = view.findViewById(R.id.usb_connection_button);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentHost.goBack();
            }
        });

        mTitle.setText(R.string.tree_view_default_title_text);
    }
}
