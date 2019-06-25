package com.art.uilibrary.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.art.uilibrary.utils.UiScreenUtils;

/**
 * Created by 56262 on 2017/11/29.
 * 目前只支持recycleview
 */

public class PullDownAnimImage extends ImageView{
    private View parent;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;
    private int minHeight;//图片的最小高度
    private int refreshHeight;//到达刷新临界点的高度
    private PullListener pullListener;
    public PullDownAnimImage(Context context) {
        super(context);
    }

    public PullDownAnimImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullDownAnimImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 目前只支持recycleview
     * @param parent imageview所处的可滑动viewgroup
     */
    public  void setPullDownAinm(View parent){
        setPullDownAinm(parent,0,0);
    }
    /**
     * 目前只支持recycleview
     * @param parent imageview所处的可滑动viewgroup
     */
    public void setPullDownAinm(View parent, int minHeight, int refreshHeight){
        this.parent = parent;
        this.minHeight = minHeight;
        if( this.minHeight == 0){
            this.minHeight = UiScreenUtils.getScreenW(getContext())*2/3;
        }
        this.refreshHeight = refreshHeight;
        if(this.refreshHeight==0 || this.refreshHeight< this.minHeight){
            this.refreshHeight = this.minHeight+UiScreenUtils.dp2Px(getContext(),100);
        }
        //final ImageView topImage = this;
        ViewGroup.LayoutParams pra = (ViewGroup.LayoutParams) this.getLayoutParams();
        pra.width = UiScreenUtils.getScreenW(getContext());
        pra.height = this.minHeight;
        this.setLayoutParams(pra);
        if(parent instanceof RecyclerView){
            final  RecyclerView mRv  = (RecyclerView)parent;
            mRv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) PullDownAnimImage.this.getLayoutParams();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            mScaling = false;
                            replyImage( PullDownAnimImage.this);
                            if(lp.height>PullDownAnimImage.this.refreshHeight){
                                if(pullListener!=null){
                                    pullListener.refresh();
                                }
                            }else {
                                if(pullListener!=null){
                                    pullListener.cancel();
                                }
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (!mScaling) {
                                //当图片也就是第一个item完全可见的时候，记录触摸屏幕的位置
                                if(mRv.getLayoutManager() instanceof StaggeredGridLayoutManager){
                                    int aa[] = ((StaggeredGridLayoutManager)mRv.getLayoutManager()).findFirstVisibleItemPositions(null);
                                    if (mRv.getChildAt(0).getY()==0f  && aa[0]==1) {
                                        mFirstPosition = event.getY();
                                    } else {
                                        break;
                                    }
                                }else if(mRv.getLayoutManager() instanceof LinearLayoutManager){
                                    if (((LinearLayoutManager)mRv.getLayoutManager()).findViewByPosition(
                                            ((LinearLayoutManager)mRv.getLayoutManager()).findFirstVisibleItemPosition()).getTop() == 0
                                            && ((LinearLayoutManager)mRv.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
                                        mFirstPosition = event.getY();
                                    } else {
                                        break;
                                    }
                                }

                            }
                            int distance = (int) ((event.getY() - mFirstPosition)* 0.6); // 滚动距离乘以一个系数
                            if (distance < 0) {
                                break;
                            }
                            // 处理放大
                            mScaling = true;
                            lp.width = UiScreenUtils.getScreenW(getContext()) + distance;
                            int deltaY = (distance * 2 / 3);
                            if(pullListener!=null && deltaY>0){
                                pullListener.change(deltaY);
                            }
                            lp.height = PullDownAnimImage.this.minHeight + deltaY;
                            PullDownAnimImage.this.setLayoutParams(lp);
                            return true; // 返回true表示已经完成触摸事件，不再处理
                    }
                    return false;
                }
            });
        }
    }

    private void replyImage(final ImageView imageView) {
        final ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
        final float w = imageView.getLayoutParams().width;// 图片当前宽度
        final float h = imageView.getLayoutParams().height;// 图片当前高度
        final float newW = UiScreenUtils.getScreenW(getContext());// 图片原宽度
        final float newH = minHeight;//图片原高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                imageView.setLayoutParams(lp);
            }
        });
        anim.start();

    }

    public interface PullListener{
        void refresh();
        void cancel();
        void change(int deltaY);
    }

    public void setPullListener(PullListener pullListener){
        this.pullListener = pullListener;
    }
}
