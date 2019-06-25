package com.art.uilibrary.xrecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by PiscesSu on 2017/4/20.
 * Version 1.0
 * * @Des 只适用于单viewtype
 */

public class RecycleViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;
    private View convertView;


    public RecycleViewHolder(View itemView) {
        super(itemView);
        this.views = new SparseArray<>();
        convertView = itemView;
    }

    //根据id检索获得该View对象
    public  <T extends View> T getElement(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public RecycleViewHolder setText(int viewId, CharSequence charSequence) {
        TextView textView = getElement(viewId);
        textView.setText(charSequence);
        return this;
    }

}
