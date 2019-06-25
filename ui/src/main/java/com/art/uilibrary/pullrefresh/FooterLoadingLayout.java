package com.art.uilibrary.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.art.uilibrary.R;


/**
 * 这个类封装了下拉刷新的布局
 * <p>
 * /****************************************************************************
 * *** Copyright 2011, 2012 Chris Banes.
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
public class FooterLoadingLayout extends LoadingLayout {
    /**
     * 进度条
     */
    private ProgressBar mProgressBar;
    /**
     * 显示的文本
     */
    private TextView mHintView;

    /**
     * 构造方法
     *
     * @param context context
     */
    public FooterLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        mProgressBar = (ProgressBar) findViewById(R.id.pull_to_load_footer_progressbar);
        mHintView = (TextView) findViewById(R.id.pull_to_load_footer_hint_textview);

        setState(State.RESET);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.ptr_pull_to_load_footer, null);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
    }

    @Override
    public int getContentSize() {
        View view = findViewById(R.id.pull_to_load_footer_content);
        if (view != null) {
            return view.getHeight();
        }

        return (int) (getResources().getDisplayMetrics().density * 70);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        mProgressBar.setVisibility(View.GONE);
        mHintView.setVisibility(View.INVISIBLE);

        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    @Override
    protected void onPullToRefresh() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_normal2);
    }

    @Override
    protected void onReleaseToRefresh() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_ready);
    }

    @Override
    public void onRefreshing() {
        mProgressBar.setVisibility(View.VISIBLE);
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pull_to_refresh_header_hint_loading);
    }

    @Override
    protected void onNoMoreData() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText(R.string.pushmsg_center_no_more_msg);
    }
}