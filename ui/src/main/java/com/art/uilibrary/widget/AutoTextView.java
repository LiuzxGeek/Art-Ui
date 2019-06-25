package com.art.uilibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.art.uilibrary.R;


/**
 * @Author liuxian
 * @Date 2017/8/11 10:51
 * @Des
 */

public class AutoTextView extends TextView {

    /**
     * normal 0
     * focus 1
     * selected 2
     */

    private String[] mTextArr;
    private int[] mBgArr;

    @SuppressWarnings("ResourceType")
    public AutoTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoTextView);
        int textArrayRes = typedArray.getInteger(R.styleable.AutoTextView_text, R.array.follow_text);
        int bgArrayRes = typedArray.getInteger(R.styleable.AutoTextView_background, R.array.follow_background);
        mTextArr = context.getResources().getStringArray(textArrayRes);
        mBgArr = context.getResources().getIntArray(bgArrayRes);
        setText(mTextArr[0]);
        setBackgroundResource(mBgArr[2]);
        typedArray.recycle();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setText(selected ? mTextArr[2] : mTextArr[0]);
        setBackgroundResource(selected ? mBgArr[2] : mBgArr[0]);
    }
}
