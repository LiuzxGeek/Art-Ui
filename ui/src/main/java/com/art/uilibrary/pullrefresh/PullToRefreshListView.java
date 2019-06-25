package com.art.uilibrary.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ListView;

import com.art.uilibrary.pullrefresh.ILoadingLayout.State;

/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 * /*******************************************
 * ************************************ Copyright 2011, 2012 Chris Banes.
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
public class PullToRefreshListView extends PullToRefreshBase<ListView> implements OnScrollListener {

    /**
     * ListView
     */
    private ListView mListView;
    /**
     * 用于滑到底部自动加载的Footer
     */
    private LoadingLayout mLoadMoreFooterLayout;
    /**
     * 滚动的监听器
     */
    private OnScrollListener mScrollListener;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshListView(Context context) {
        this(context, null);
        setOverScrollMode(ListView.OVER_SCROLL_NEVER);//适配魅族可以使用刷新功能
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setOverScrollMode(ListView.OVER_SCROLL_NEVER);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(ListView.OVER_SCROLL_NEVER);
    }

    @Override
    protected ListView createRefreshableView(Context context, AttributeSet attrs) {
        ListView listView = new ListView(context);
        mListView = listView;
        listView.setOnScrollListener(this);
        return listView;
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (mLoadMoreFooterLayout != null) {
                mLoadMoreFooterLayout.setState(State.NO_MORE_DATA);
            }
            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (footerLoadingLayout != null) {
                footerLoadingLayout.setState(State.NO_MORE_DATA);
            }
        }
    }

    /**
     * 设置滑动的监听器
     *
     * @param l 监听器
     */
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (mLoadMoreFooterLayout != null) {
            mLoadMoreFooterLayout.setState(State.REFRESHING);
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        if (mLoadMoreFooterLayout != null) {
            mLoadMoreFooterLayout.setState(State.RESET);
        }
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        if (isScrollLoadEnabled() == scrollLoadEnabled) {
            return;
        }

        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (mLoadMoreFooterLayout == null) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
                mListView.addFooterView(mLoadMoreFooterLayout, null, false);
            }

            mLoadMoreFooterLayout.show(true);
        } else {
            if (mLoadMoreFooterLayout != null) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }

        return super.getFooterLoadingLayout();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                if (isReadyForPullUp()) {
                    startLoading();
                }
            }
        }

        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new HeaderLoadingLayout(context);
    }

    /**
     * 表示是否还有更多数据
     *
     * @return true表示还有更多数据
     */
    private boolean hasMoreData() {
        return !((mLoadMoreFooterLayout != null) && (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA));

    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (adapter == null || adapter.isEmpty()) {
            return true;
        }

        int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
        return mostTop >= 0;

    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (adapter == null || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition ==
         * lastItemPosition, but ListView internally uses a FooterView which
         * messes the positions up. For me we'll just subtract one to account
         * for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - mListView.getFirstVisiblePosition();
            final int childCount = mListView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mListView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mListView.getBottom();
            }
        }

        return false;
    }
}
