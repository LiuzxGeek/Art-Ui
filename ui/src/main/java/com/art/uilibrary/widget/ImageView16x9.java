package com.art.uilibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @Author liuxian
 * @Date 2017/8/16 16:14
 * @Des
 */

public class ImageView16x9 extends ImageView {
    public ImageView16x9(Context context) {
        super(context);
    }

    public ImageView16x9(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;
        if (widthMeasureSpec / 16 > heightMeasureSpec / 9) {
            width = 16 * heightMeasureSpec / 9;
        } else if (widthMeasureSpec / 16 < heightMeasureSpec / 9) {
            height = 9 * widthMeasureSpec / 16;
        }
        super.onMeasure(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
