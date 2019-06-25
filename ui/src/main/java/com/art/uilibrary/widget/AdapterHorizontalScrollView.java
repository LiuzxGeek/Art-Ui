package com.art.uilibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * @Author liuxian
 * @Date 2017/10/2518:11
 * @Desc
 */

public class AdapterHorizontalScrollView extends HorizontalScrollView {
    private LinearLayout mContainer;
    private int mScreenWidth;
    private int mCurrentIndex;
    private int mChildCount;
    private Scroller mScroller;
    private int mLastViewX;

    public AdapterHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScroller = new Scroller(context);
        mLastViewX = mScreenWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContainer = (LinearLayout) getChildAt(0);
        mChildCount = mContainer.getChildCount();
        Log.d("ScrollView", "getChildCount:" + mChildCount);
    }

    float mLastX;
    boolean mScrollLeft;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float rawX = ev.getRawX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("ScrollView", "ACTION_DOWN X:" + ev.getX() + "\t ScrollX: \t" + getScrollX());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("ScrollView", "ACTION_MOVE X:" + ev.getX() + "\t ScrollX: \t" + getScrollX());
                mScrollLeft = rawX - mLastX <= 0;
                mLastX = rawX;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d("ScrollView", "ACTION_UP X:" + ev.getX() + "\t ScrollX: \t" + getScrollX());
                if (mScrollLeft) {
                    if (loadNext())
                        return true;
                } else {
                    if (loadPre())
                        return true;
                }
                break;

        }
        return super.onTouchEvent(ev);
    }

    private boolean loadNext() {
        if (++mCurrentIndex > mChildCount) {
            mCurrentIndex--;
            return false;
        }
        int scrollToX = mLastViewX + mScreenWidth;
        mScroller.startScroll(getScrollX(), 0, scrollToX, 0, 300);
//        scrollTo(scrollToX, 0);

        mLastViewX = scrollToX;
        Log.d("ScrollView", "loadNext" + scrollToX);
        return true;
    }

    private boolean loadPre() {
        if (--mCurrentIndex < 0) {
            mCurrentIndex = 0;
            return false;
        }
        int scrollToX = mLastViewX - mScreenWidth;
        mScroller.startScroll(getScrollX(), 0, scrollToX, 0, 300);
//        scrollTo(scrollToX, 0);
        mLastViewX = scrollToX;
        Log.d("ScrollView", "loadPre" + scrollToX);
        return true;
    }

}
