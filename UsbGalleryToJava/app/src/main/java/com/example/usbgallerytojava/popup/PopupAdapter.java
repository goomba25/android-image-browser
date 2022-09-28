package com.example.usbgallerytojava.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.usbgallerytojava.R;

import java.util.ArrayList;
import java.util.List;

public class PopupAdapter extends BaseAdapter {

    private Context mContext;
    private List<PopupItem> mItems = new ArrayList<>();
    private boolean mEnabled = true;
    private boolean mSelectable = false;

    public PopupAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View savedView = view;
        ViewHolder holder;

        if ( savedView == null ) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            savedView = inflater.inflate(R.layout.popup_item, viewGroup, false);
            holder = new ViewHolder(savedView);
            savedView.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        PopupItem item = mItems.get(i);
        holder.setTitle(item.getTitle());

        if ( mEnabled ) {
            holder.setEnable(true);
            if ( mSelectable ) {
                if ( i == 0 ) {
                    holder.select();
                }
            }
        }
        else {
            holder.setEnable(false);
        }

        return savedView;
    }

    public void updateItem(List<PopupItem> items) {
        mItems = items;
    }

    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

    public void setEnable(boolean enable) {
        mEnabled = enable;
    }

    public class ViewHolder {
        private View mRootView;
        private TextView mTitle;

        public ViewHolder(View itemView) {
            mRootView = itemView;
            mTitle = mRootView.findViewById(R.id.title);
        }

        public void setTitle(String title) {
            mTitle.setText(title);
        }

        public void select() {
            mTitle.setTextColor(mContext.getResources().getColor(R.color.popup_item_select_text_color));
        }

        public void setEnable(boolean enable) {
            mRootView.setEnabled(enable);
            mTitle.setEnabled(enable);
        }
    }
}
