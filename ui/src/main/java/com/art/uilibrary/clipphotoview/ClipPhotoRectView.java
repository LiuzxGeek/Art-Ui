package com.art.uilibrary.clipphotoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;


/**
 * Created by wdl on 2017/9/6.
 */

public class ClipPhotoRectView extends View{
    private static final int STROKE_WIDTH = 5;
    private Context context;
    private int mRadius;

    public ClipPhotoRectView(Context context) {
        super(context);
        this.context = context;
    }

    public ClipPhotoRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ClipPhotoRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMask(canvas);
    }

    /**
     * 绘制蒙版
     */
    private void drawMask(Canvas canvas) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        mRadius = (screenWidth-200)/2;
        //画背景颜色
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c1 = new Canvas(bitmap);
        c1.drawARGB(150, 0, 0, 0);
        Paint strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(STROKE_WIDTH);
        //c1.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, strokePaint);
        c1.drawRect(100,screenHeight/2-mRadius,screenWidth-100,screenHeight/2+mRadius,strokePaint);

        //画矩形
        Bitmap circleBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c2 = new Canvas(circleBitmap);
        Paint circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.RED);
        circlePaint.setAntiAlias(true);
        //c2.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, circlePaint);
        c2.drawRect(100,screenHeight/2-mRadius,screenWidth-100,screenHeight/2+mRadius,circlePaint);
        //两个图层合成
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        c1.drawBitmap(circleBitmap, 0, 0, paint);
        paint.setXfermode(null);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}
