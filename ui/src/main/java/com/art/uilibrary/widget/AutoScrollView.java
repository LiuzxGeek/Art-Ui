package com.art.uilibrary.widget;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * @Author liuxian
 * @Date 2017/8/9 17:19
 * @Des
 */

public class AutoScrollView extends ScrollView {

    private OnBorderListener onBorderListener;
    private View contentView;
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;

    public AutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFillViewport(true);
    }

    /**
     * 解决这个问题 java.lang.IllegalArgumentException: Wrong state class, expecting
     * View State but received class android.widget.ScrollView$SavedState
     * instead. This usually happens when two views of different type have the
     * same id in the same hierarchy.
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try {
            super.onRestoreInstanceState(state);
        } catch (Exception e) {
        }
        state = null;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (xDistance > yDistance) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        doOnBorderListener();
    }

    public void setOnBorderListener(final OnBorderListener onBorderListener) {
        this.onBorderListener = onBorderListener;
        if (onBorderListener == null) {
            return;
        }

        if (contentView == null) {
            contentView = getChildAt(0);
        }
    }

    private void doOnBorderListener() {
        if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
            if (onBorderListener != null) {
                onBorderListener.onBottom();
            }
        } else if (getScrollY() == 0) {
            if (onBorderListener != null) {
                onBorderListener.onTop();
            }
        }
    }

    /**
     * OnBorderListener, Called when scroll to top or bottom
     *
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a>
     *         2013-5-22
     */
    public static interface OnBorderListener {

        /**
         * Called when scroll to bottom
         */
        public void onBottom();

        /**
         * Called when scroll to top
         */
        public void onTop();
    }

    // /**
    // * Integer.MAX_VALUE >> 2,如果不设置，系统默认设置是显示两条
    // */
    // public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
    // MeasureSpec.AT_MOST);
    // super.onMeasure(widthMeasureSpec, expandSpec);
    // }
}
