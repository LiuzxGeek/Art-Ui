package com.art.uilibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.uilibrary.R;

/**
 * @Author liuxian
 * @Date 2017/8/8 17:04
 * @Des
 */

public class ToolBar extends FrameLayout implements View.OnClickListener {

    private final LayoutInflater mInflater;
    private final RelativeLayout mBarView;
    private final TextView mTitleTv;
    private final TextView mTitleRightTv;
    private final TextView mTitleRightIconFont;
    private final TextView mTitleLeftTv;

    private final ImageView mTitleRightIv;
    private final ImageView mTitleLeftIv;

    private OnClickListener mOnClickListener;

    public ToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBarView = (RelativeLayout) mInflater.inflate(R.layout.tb_toolbar, null);
        addView(mBarView);
        mTitleTv = mBarView.findViewById(R.id.toolbar_title);
        mTitleRightTv = mBarView.findViewById(R.id.toolbar_tv_right);
        mTitleLeftTv = mBarView.findViewById(R.id.toolbar_tv_left);
        mTitleRightIconFont = mBarView.findViewById(R.id.toolbar_icon_font_right);

        mTitleRightIv = mBarView.findViewById(R.id.toolbar_iv_right);
        mTitleLeftIv = mBarView.findViewById(R.id.toolbar_iv_left);
        mBarView.findViewById(R.id.toolbar_home_left).setOnClickListener(this);
        mBarView.findViewById(R.id.toolbar_home_right).setOnClickListener(this);
    }

    public TextView getmTitleRightIconFont() {
        return mTitleRightIconFont;
    }

    public void setToolBarClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public void setTitle(Object titleRes) {
        setText(mTitleTv, titleRes);
    }

    private void setText(TextView titleTv, Object titleRes) {
        if (titleRes instanceof Integer) {
            if ((Integer) titleRes != 0)
                titleTv.setText((Integer) titleRes);
        } else if (titleRes instanceof String) {
            titleTv.setText((String) titleRes);
        }
    }

    private void setColor(TextView titleTv, int colorRes) {
        titleTv.setTextColor(colorRes);
    }

    public void setRightTitle(Object titleRes) {
        setText(mTitleRightTv, titleRes);
    }

    public void setRightColor(int colorRes) {
        setColor(mTitleRightTv, colorRes);
    }

    public void setRightEnabled(boolean enabled) {
        mTitleRightTv.setEnabled(enabled);
    }

    public TextView getRightTitle() {
        return mTitleRightTv;
    }

    public ImageView getLeftIv() {
        return mTitleLeftIv;
    }

    public void setLeftTitle(Object titleRes) {
        setText(mTitleLeftTv, titleRes);
    }

    public void setRightIv(int resId) {
        mTitleRightIv.setImageResource(resId);
    }


    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) mOnClickListener.onClick(v);
    }
}
