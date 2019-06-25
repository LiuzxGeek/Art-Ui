package com.art.uilibrary.base;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liuxian
 * @Date 2017/8/30 16:05
 * @Des
 */

public abstract class OralAdapter<D> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected View convertView;

    protected List<D> list;

    protected OralAdapter(Context context) {
        this.mContext = context;
        list = new ArrayList<>();
        mInflater = LayoutInflater.from(mContext);
    }


    public void invalidateData(List<D> list) {
        this.list = list;
        // TODO Auto-generated method stub
        if (Looper.myLooper() == Looper.getMainLooper())//防止后台运行，可能页面已经关闭，而它仍在运行
            notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected View getInflateView(int resource) {
        convertView = LayoutInflater.from(mContext).inflate(resource, null);
        return convertView;
    }

    protected View getFromView(int resource) {
        View convertView = LayoutInflater.from(mContext).inflate(resource, null);
        return convertView;
    }
}