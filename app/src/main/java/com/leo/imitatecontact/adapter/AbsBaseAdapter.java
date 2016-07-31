package com.leo.imitatecontact.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by LEO on 2016/7/30.
 */

public abstract class AbsBaseAdapter<T> extends BaseAdapter{

    protected List<T> mDataList;  //数据集合
    protected Context mContext;   //上下文
    private int mLayoutId;      //资源Id

    public AbsBaseAdapter(List<T> mDataList, Context mContext, int mLayoutId) {
        this.mDataList = mDataList;
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return getTypeCount();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactViewHolder viewHolder = ContactViewHolder.createOrGetViewHolder(mContext,position, convertView, mLayoutId);
        convertViewValue(viewHolder);
        return viewHolder.getmConvertView();
    }

    public abstract void convertViewValue(ContactViewHolder contactViewHolder);
    public abstract int getTypeCount();
}
