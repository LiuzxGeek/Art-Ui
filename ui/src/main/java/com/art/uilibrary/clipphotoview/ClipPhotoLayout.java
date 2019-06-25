package com.art.uilibrary.clipphotoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by wdl on 2017/9/6.
 */

public class ClipPhotoLayout extends FrameLayout {
    private ClipPhotoCircleView mCircleView;
    private ClipPhotoRectView mRectView;
    private ClipPhotoView mPhotoView;
    public static final int CIRCLE = 0;
    public static final int RECT = 1;

    public ClipPhotoView getmPhotoView() {
        return mPhotoView;
    }

    public ClipPhotoLayout(Context context) {
        this(context, null);
    }

    public ClipPhotoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipPhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addClipView(int type){
        if(type == CIRCLE){
            setCircle();
        }else if(type == RECT){
            setRect();
        }else {
            setRect();
        }
    }

    private void setRect(){
        mRectView = new ClipPhotoRectView(getContext());
        mPhotoView = new ClipPhotoView(getContext());

        android.view.ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mPhotoView, lp);
        addView(mRectView, lp);
    }

    private void setCircle(){
        mCircleView = new ClipPhotoCircleView(getContext());
        mRectView = new ClipPhotoRectView(getContext());
        mPhotoView = new ClipPhotoView(getContext());

        android.view.ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mPhotoView, lp);
        addView(mCircleView, lp);
    }


    public void setImageDrawable(Drawable drawable) {
        mPhotoView.setImageDrawable(drawable);
    }

    public void setImageDrawable(int resId) {
        setImageDrawable(getContext().getResources().getDrawable(resId));
    }

    public void setImageBitmap(Bitmap bitmap ){
        mPhotoView.setImageBitmap(bitmap);
    }

    //返回存储在sd卡中
    public String clipBitmap() {
        return mPhotoView.clip();
    }
}
