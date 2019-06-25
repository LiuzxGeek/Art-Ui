package com.art.uilibrary.nui.multiphotopicker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.art.uilibrary.R;
import com.art.uilibrary.base.IAdapterListener;
import com.art.uilibrary.xrecyclerview.adapter.RecycleViewAdapter;
import com.art.uilibrary.xrecyclerview.adapter.RecycleViewHolder;
import com.art.uilibrary.nui.multiphotopicker.model.ImageItem;
import com.art.uilibrary.utils.UiUtils;
import com.art.uilibrary.utils.UiScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liuxian
 * @Date 2017/8/31 10:36
 * @Des
 */

public class ImageAllAdapter extends RecycleViewAdapter<ImageItem> implements IAdapterListener.IOnChildClickListener<Integer> {

    private List<ImageItem> mSelectList = new ArrayList<>();
    private int mCanAddCount;//用于计算还能添加几张照片
    private boolean mMultiChoice = true;
    private int mImgWidth, mImgHeight;
    private int mMaxImgCounts;

    private int mDisplayType;//0选择1浏览

    public void setCanAddCount(int canAddCount) {
        this.mCanAddCount = canAddCount;
    }

    public void setMultiChoice(boolean multiChoice) {
        mMultiChoice = multiChoice;
    }

    public void setSelectList(List<ImageItem> selectList) {
        mSelectList = selectList;
    }

    public ImageAllAdapter(Context context, int layoutResID, int maxImgCounts, int displayType) {
        super(context, layoutResID);
        mDisplayType = displayType;
        mMaxImgCounts = maxImgCounts;
        setOnChildClickListener(this);
        if (mDisplayType == 0)
            setHeadLayoutResID(R.layout.up_item_camera);
        mImgWidth = UiScreenUtils.dp2Px(mContext, 93);
        mImgHeight = mImgWidth;
    }

    @Override
    protected void convert(final RecycleViewHolder holder, final ImageItem item, int position) {
        TextView numView = holder.getElement(R.id.num);
        ImageView ivGif = holder.getElement(R.id.iv_gif);
        numView.setText(item.mSelectIndex != 0 ? String.valueOf(item.mSelectIndex) : "");
        ImageView imageView = holder.getElement(R.id.img);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .override(mImgWidth, mImgHeight);
        Glide.with(mContext)
                .asBitmap()
                .load("file://" + item.sourcePath).apply(options)
                .into(imageView);
        //ImageDisplayer.getInstance(mContext).displayBmp(imageView, item.thumbnailPath, item.sourcePath, true, mImgWidth, mImgHeight);
        holder.getElement(R.id.layout).setSelected(item.isSelected);
        if (!TextUtils.isEmpty(item.sourcePath) && item.sourcePath.contains(".gif")) {
            ivGif.setVisibility(View.VISIBLE);
        } else {
            ivGif.setVisibility(View.INVISIBLE);
        }
        if (mCanAddCount <= 0) {
            holder.getElement(R.id.shadow_view).setVisibility(item.isSelected ? View.GONE : View.VISIBLE);
        } else {
            holder.getElement(R.id.shadow_view).setVisibility(View.GONE);
        }
        if (!mMultiChoice) {
            numView.setVisibility(View.GONE);
        }
        if (mDisplayType == 0) {
            holder.getElement(R.id.num_layout).setVisibility(View.VISIBLE);
        }
        holder.getElement(R.id.num_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getLayoutPosition();
                position -= 1;
                ImageItem imageItem = list.get(position);
                if (imageItem.isSelected) {
                    mCanAddCount++;
                    mSelectList.remove(imageItem);
                    list.remove(imageItem);

                    imageItem.isSelected = false;

                    int targetIndex = imageItem.mSelectIndex;

                    imageItem.mSelectIndex = 0;
                    list.add(position, imageItem);
                    if (mCanAddCount != 1)//等于1全部刷新，不等于1则局部刷新
                        notifyItemChanged(position + 1);
                    for (int i = 0, size = mSelectList.size(); i < size; i++) {
                        ImageItem ii = mSelectList.get(i);
                        if (ii.mSelectIndex > targetIndex) {//刷新比改变目标大的item
                            list.remove(ii);
                            ii.mSelectIndex -= 1;
                            list.add(ii.index, ii);
                            if (mCanAddCount != 1)
                                notifyItemChanged(ii.index + 1);//考虑到还有一个相机item
                        }
                    }
                    if (mCanAddCount == 1) {//全选取消1个的刷新
                        notifyDataSetChanged();
                    }
                } else {
                    if (isFullChosen()) {
                        String beyondRes = mContext.getResources().getString(R.string.str_photo_count_beyond, mMaxImgCounts);
                        UiUtils.showConfirmDialog(mContext, beyondRes, R.string.str_known, false, true);
                        return;
                    }
                    mCanAddCount--;
                    list.remove(imageItem);
                    mSelectList.remove(imageItem);

                    imageItem.isSelected = true;
                    //不要让selectIndex与selectCount发生关系，否则添加个数展示会出错
                    imageItem.mSelectIndex = mSelectList.size() + 1;
                    mSelectList.add(imageItem);
                    list.add(position, imageItem);
                    if (isFullChosen()) {
                        notifyDataSetChanged();
                    } else {
                        notifyItemChanged(position + 1);
                    }
                }
                mCheckListener.onImageChecked(mSelectList);
            }
        });
    }

    @Override
    public void onChildClickListener(Integer target) {
        if (isFullChosen()) {
            showFullToast();
            return;
        }
        int position = target.intValue();
        if (mDisplayType == 0) {
            if (position == 0) {
                mCheckListener.takePhoto();
                return;
            }
            target -= 1;
        }
        if (!mMultiChoice) {
            mCheckListener.onImageGot(list.get(target).sourcePath);
        } else {
            mCheckListener.startImageViewActivity(list, mSelectList, target, mCanAddCount);
        }
    }

    private boolean isFullChosen() {
        return mCanAddCount <= 0;
    }

    private void showFullToast() {
        String beyondRes = mContext.getResources().getString(R.string.str_photo_count_beyond, mMaxImgCounts);
        UiUtils.showConfirmDialog(mContext, beyondRes, R.string.str_known, false, true);
    }

    IImageCheckListener mCheckListener;

    public void setCheckListener(IImageCheckListener checkListener) {
        mCheckListener = checkListener;
    }

    public interface IImageCheckListener {

        void takePhoto();

        void onImageChecked(List<ImageItem> list);

        void startImageViewActivity(List<ImageItem> list, List<ImageItem> selectList, int index, int canAddCount);

        void onImageGot(String url);
    }
}
