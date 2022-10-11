package com.example.usbgallerytojava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TreeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int COLUMN_INDEX = 4;
    private static final int DIR_TYPE = 0;
    private static final int FILE_TYPE = 1;


    private Context mContext;
    private LayoutInflater mInflater;
    private final GridSpanSizeLookup mSpanSizeLookup = new GridSpanSizeLookup();

    private List<DirectoryTile> mDirs = new ArrayList<>();
    private List<FileTile> mFiles = new ArrayList<>();

    public TreeViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case DIR_TYPE:
                return new DirectoryHolder(mInflater.inflate(R.layout.tree_dir_item, parent, false));
            case FILE_TYPE:
                return new FileHolder(mInflater.inflate(R.layout.tree_file_item, parent, false));
            default:
                throw new RuntimeException("Unknown Type" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case DIR_TYPE:
                DirectoryHolder dh = (DirectoryHolder) holder;
                DirectoryTile dt = mDirs.get(position);
                break;
            case FILE_TYPE:
                FileHolder fh = (FileHolder) holder;
                FileTile ft = mFiles.get(position - mDirs.size());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mDirs.size()) {
            return DIR_TYPE;
        }
        else {
            return FILE_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return mDirs.size() + mFiles.size();
    }

    public GridLayoutManager getGridLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, COLUMN_INDEX);
        gridLayoutManager.setSpanSizeLookup(mSpanSizeLookup);
        return gridLayoutManager;
    }

    public void update(List<DirectoryTile> dirs, List<FileTile> files) {
        mDirs = dirs;
        mFiles = files;
        notifyDataSetChanged();
    }

    private class DirectoryHolder extends RecyclerView.ViewHolder {
        final TextView mDirName;
        final TextView mCount;

        public DirectoryHolder(@NonNull View itemView) {
            super(itemView);

            mDirName = itemView.findViewById(R.id.name);
            mCount = itemView.findViewById(R.id.count);
        }
    }

    private class FileHolder extends RecyclerView.ViewHolder {
        final ImageView mImageView;

        public FileHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.preview);
        }
    }

    class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            return 1;
        }

        @Override
        public int getSpanIndex(int position, int spanCount) {
            return COLUMN_INDEX;
        }
    }
}
