package com.art.uilibrary.pullrefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.art.uilibrary.R;


/**
 * 这个类封装了下拉刷新的布局
 * /***************************************************************
 * **************** Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
public class HeaderLoadingLayout extends LoadingLayout {
    /**
     * 旋转动画时间
     */
    private static final int ROTATE_ANIM_DURATION = 150;
    /**
     * Header的容器
     */
    private LinearLayout mHeaderContainer;
    /**
     * 箭头图片
     */
    private ImageView mArrowImageView;
    /**
     * 进度条
     */
    private ProgressBar mProgressBar;
    /**
     * 状态提示TextView
     */
    private TextView mHintTextView;
    /**
     * 最后更新时间的TextView
     */
    private TextView mHeaderTimeView;
    /**
     * 最后更新时间的标题
     */
    private TextView mHeaderTimeViewTitle;
    /**
     * 向上的动画
     */
    private Animation mRotateUpAnim;
    /**
     * 向下的动画
     */
    private Animation mRotateDownAnim;
    /**
     * 顶部动画
     */
    private AnimationDrawable mTopAnim;
    /**
     * 顶部动画图片
     */
    private ImageView mTopAnimImg;

    private Context mContext;

    /**
     * 构造方法
     *
     * @param context context
     */
    public HeaderLoadingLayout(Context context) {
        super(context);
        this.mContext = context;
        init(context);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public HeaderLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        mHeaderContainer = (LinearLayout) findViewById(R.id.pull_to_refresh_header_content);
        mArrowImageView = (ImageView) findViewById(R.id.pull_to_refresh_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.pull_to_refresh_header_hint_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.pull_to_refresh_header_progressbar);
        mHeaderTimeView = (TextView) findViewById(R.id.pull_to_refresh_header_time);
        mHeaderTimeViewTitle = (TextView) findViewById(R.id.pull_to_refresh_last_update_time_text);
        mTopAnimImg = (ImageView) findViewById(R.id.top_anim);
        mTopAnim = (AnimationDrawable) mTopAnimImg.getDrawable();

        float pivotValue = 0.5f; // SUPPRESS CHECKSTYLE
        float toDegree = -180f; // SUPPRESS CHECKSTYLE
        // 初始化旋转动画
        mRotateUpAnim = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue, Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(toDegree, 0.0f, Animation.RELATIVE_TO_SELF, pivotValue, Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
//        Glide.with(mContext).load(R.mipmap.icon_ptr_refresh)
//                .into(mTopAnimImg);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        // 如果最后更新的时间的文本是空的话，隐藏前面的标题
        mHeaderTimeViewTitle.setVisibility(TextUtils.isEmpty(label) ? View.INVISIBLE : View.VISIBLE);
        mHeaderTimeView.setText(label);
    }

    @Override
    public int getContentSize() {
        if (mHeaderContainer != null) {
            return mHeaderContainer.getHeight();
        }

        return (int) (getResources().getDisplayMetrics().density * 60);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.ptr_pull_to_refresh_header, null);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        mArrowImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mArrowImageView.clearAnimation();
        mTopAnim.stop();
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onPullToRefresh() {
        if (getPreState() == State.RELEASE_TO_REFRESH) {
            mArrowImageView.clearAnimation();
            mArrowImageView.startAnimation(mRotateDownAnim);
        }
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onReleaseToRefresh() {
        mArrowImageView.clearAnimation();
        mArrowImageView.startAnimation(mRotateUpAnim);
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_ready);
    }

    @Override
    public void onRefreshing() {
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTopAnim.start();
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_loading);
    }
}
