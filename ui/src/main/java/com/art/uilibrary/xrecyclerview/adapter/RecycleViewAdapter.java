package com.art.uilibrary.xrecyclerview.adapter;

import android.content.Context;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.art.uilibrary.R;
import com.art.uilibrary.base.IAdapterListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuChangz on 2017/4/20.
 * Version 1.0
 */

public abstract class RecycleViewAdapter<T> extends RecyclerView.Adapter<RecycleViewHolder> {

    protected Context mContext;
    protected List<T> list;

    private static final int HEADER_VIEW_TYPE = -500;
    private static final int EMPTY_VIEW_TYPE = -1000;
    private int mEmptyLayoutResId = 0;
    private int mHeadLayoutResId = 0;
    private View headView;

    private int mLayoutResId;


    private IAdapterListener.IOnChildClickListener mChildClickListener;
    private IAdapterListener.IOnItemClickListener mItemClickListener;

    private LayoutInflater mInflater;

    public void setHeadLayoutResID(int headLayoutResId) {
        mHeadLayoutResId = headLayoutResId;
    }

    public void addheadView(View headView) {
        this.headView = headView;
    }

    public RecycleViewAdapter(Context context, int layoutResId) {
        mContext = context;
        this.mLayoutResId = layoutResId;
        mInflater = LayoutInflater.from(mContext);
        list = new ArrayList<>();
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecycleViewHolder holder;
        //load empty view
        if (viewType == HEADER_VIEW_TYPE) {
            if (headView == null) {
                View headView = mInflater.inflate(mHeadLayoutResId, parent, false);
                holder = new RecycleViewHolder(headView);
            } else {
                holder = new RecycleViewHolder(headView);
            }

        } else if (viewType == EMPTY_VIEW_TYPE) {
            View emptyView = mInflater.inflate(mEmptyLayoutResId, parent, false);
            holder = new RecycleViewHolder(emptyView);
        } else {
            final View itemView = mInflater.inflate(mLayoutResId, parent, false);
            holder = new RecycleViewHolder(itemView);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        setOnChildClickListener(holder.getElement(R.id.layout), position);
        if (list != null && list.size() > 0) {
            if (mHeadLayoutResId != 0) {
                if (position > 0) {
                    convert(holder, list.get(position - 1), position - 1);
                }
            } else {
                convert(holder, list.get(position), position);
            }

        }

    }


    //将获取到的数据集合加到之前的集合中来
    public void appendList(List<T> beanList) {
        if (beanList == null || beanList.size() == 0) return;
        int positionStart = list.size();
        list.addAll(beanList);
        int itemCount = beanList.size();
        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(positionStart, itemCount + 1);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            if (mEmptyLayoutResId != 0 || mHeadLayoutResId != 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (list.size() == 0 && mEmptyLayoutResId != 0) {
                return 1;
            }
            if (mHeadLayoutResId != 0) {
                return list.size() + 1;
            }
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && (mHeadLayoutResId != 0 || headView != null)) {
            return HEADER_VIEW_TYPE;
        } else if ((list == null || list.size() == 0) && mEmptyLayoutResId != 0) {
            return EMPTY_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }


    public void invalidateData(List<T> list) {
        this.list = list;
        // TODO Auto-generated method stub
        if (Looper.myLooper() == Looper.getMainLooper())//防止后台运行，可能页面已经关闭，而它仍在运行
            notifyDataSetChanged();
    }

    protected abstract void convert(RecycleViewHolder holder, T item, int position);

    //set EmptyView
    public void setEmptyView(int layoutResID) {
        this.mEmptyLayoutResId = layoutResID;
    }


    public void setOnChildClickListener(IAdapterListener.IOnChildClickListener listener) {
        mChildClickListener = listener;
    }

    public void setItemClickListener(IAdapterListener.IOnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    protected void setOnChildClickListener(View view, final Object data) {
        if (view == null) return;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChildClickListener.onChildClickListener(data);
            }
        });
    }

    protected <TT extends Object, TD extends Object> void setOnItemClickListener(View view, final TT type, final TD... data) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClickListener(type, data);
            }
        });
    }

}
