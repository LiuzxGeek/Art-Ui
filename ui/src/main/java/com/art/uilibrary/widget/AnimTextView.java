package com.art.uilibrary.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.art.uilibrary.R;


/**
 * @Author wdl
 * @Date 2017/10/26
 */

public class AnimTextView extends TextView {
    private Context mContext;

    @SuppressWarnings("ResourceType")
    public AnimTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void startAnim(){
        setVisibility(View.VISIBLE);
        Animation showAnimation = AnimationUtils.loadAnimation(mContext, R.anim.personl_message_show);
        this.startAnimation(showAnimation);
        showAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
