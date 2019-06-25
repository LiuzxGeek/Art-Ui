package com.art.uilibrary.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * @Author liuxian
 * @Date 2017/10/1917:31
 * @Desc 解决键盘压住输入框的问题
 */

public class AndroidBug5497Workaround {
    private int mStatusHeight;
    //为适应华为小米等手机键盘上方出现黑条或不适配
    private int contentHeight;//获取setContentView本来view的高度

    public void setStatusHeight(int statusHeight) {
        if (mStatusHeight != statusHeight) {
            mStatusHeight = statusHeight;
        }
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    public void assistActivity(Activity activity, int statusHeight) {
        mStatusHeight = statusHeight;
        FrameLayout content = activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                contentHeight = mChildOfContent.getHeight();//兼容华为等机型
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = Math.abs(usableHeightSansKeyboard - usableHeightNow);
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    // keyboard probably just became visible
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + mStatusHeight;
                } else {
                    // keyboard probably just became hidden
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                }
            } else {
                frameLayoutParams.height = contentHeight;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        //非全屏r.bottom  - r.top
        return (r.bottom - r.top);// 全屏模式下： return r.bottom
    }
}
