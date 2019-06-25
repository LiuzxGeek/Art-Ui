package com.art.uilibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @Author liuxian
 * @Date 2017/8/30 15:03
 * @Des
 */

public class StretchGridView extends GridView {
    public StretchGridView(Context context) {
        super(context);
    }

    public StretchGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
