package com.leo.imitatecontact.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by LEO on 2016/7/30.
 */
public class ContactViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    public ContactViewHolder(Context context,int position, int layoutId) {
        this.mPosition = position;
        mViews = new SparseArray<>();
        mConvertView = View.inflate(context, layoutId, null);
        mConvertView.setTag(this);
    }

    public static ContactViewHolder createOrGetViewHolder(Context context, int position, View convertView, int layoutId) {
        ContactViewHolder contactViewHolder;
        if (convertView == null) {
            contactViewHolder = new ContactViewHolder(context,position, layoutId);
        } else {
            contactViewHolder = (ContactViewHolder) convertView.getTag();
            contactViewHolder.mPosition = position;
        }
        return contactViewHolder;
    }

    public View getmConvertView() {
        return mConvertView;
    }


    public int getmPosition() {
        return mPosition;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T)view;
    }

    public ContactViewHolder setText(int viewId, String text)
    {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
}
