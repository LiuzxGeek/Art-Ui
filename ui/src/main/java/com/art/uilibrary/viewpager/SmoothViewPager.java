package com.art.uilibrary.viewpager;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Author liuxian
 * @Date 2018/3/117:19
 * @Desc
 */

public class SmoothViewPager extends ViewPager {
    /**
     */
    private float mLastMotionY = -1;

    private float mLastMotionX = -1;

    /**
     */
    private boolean mIsHandledTouchEvent = false;

    public SmoothViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = event.getY();
                mLastMotionX = event.getX();
                mIsHandledTouchEvent = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = event.getY() - mLastMotionY;
                final float absDiffY = Math.abs(deltaY);
                final float deltaX = event.getX() - mLastMotionX;
                final float absDiffX = Math.abs(deltaX);
                if (absDiffY > absDiffX) {
                    mLastMotionY = event.getY();
                    mLastMotionX = event.getX();
                    mIsHandledTouchEvent = true;
                } else {
                    mLastMotionX = event.getX();
                    mLastMotionY = event.getY();
                    mIsHandledTouchEvent = false;
                }
        }
        getParent().requestDisallowInterceptTouchEvent(!mIsHandledTouchEvent);
        return super.dispatchTouchEvent(event);
    }

}
