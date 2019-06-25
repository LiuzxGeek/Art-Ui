package com.art.uilibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by 56262 on 2018/1/4.
 */

public class WhScrollView extends ScrollView{

    private ScrollViewListener scrollViewListener = null;

    public WhScrollView(Context context) {
        super(context);
    }

    public WhScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public WhScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface ScrollViewListener {

        void onScrollChanged(WhScrollView scrollView, int x, int y, int oldx, int oldy);

    }
}
