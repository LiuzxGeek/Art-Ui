package com.art.uilibrary.pullrefresh;

import android.view.View;

/**
 * 定义了拉动刷新的接口
 * /*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
interface IPullToRefresh<T extends View> {

    /**
     * 判断当前下拉刷新是否可用
     *
     * @return true如果可用，false不可用
     */
     boolean isPullRefreshEnabled();

    /**
     * 判断当前下拉很远是否可用
     *
     * @return true如果可用，false不可用
     */
    boolean isPullDeepEnabled();

    /**
     * 设置当前下拉很远是否可用
     *
     * @param pullDeepEnabled true表示可用，false表示不可用
     */
    void setPullDeepEnabled(boolean pullDeepEnabled);

    /**
     * 设置当前下拉刷新是否可用
     *
     * @param pullRefreshEnabled true表示可用，false表示不可用
     */
     void setPullRefreshEnabled(boolean pullRefreshEnabled);

    /**
     * 判断上拉加载是否可用
     *
     * @return true可用，false不可用
     */
     boolean isPullLoadEnabled();

    /**
     * 设置当前上拉加载更多是否可用
     *
     * @param pullLoadEnabled true表示可用，false表示不可用
     */
     void setPullLoadEnabled(boolean pullLoadEnabled);

    /**
     * 滑动到底部加载是否可用
     *
     * @return true可用，否则不可用
     */
     boolean isScrollLoadEnabled();

    /**
     * 滑动到底部是否自动加载更多数据
     *
     * @param scrollLoadEnabled 如果这个值为true的话，那么上拉加载更多的功能将会禁用
     */
     void setScrollLoadEnabled(boolean scrollLoadEnabled);

    /**
     * 设置刷新的监听器
     *
     * @param refreshListener 监听器对象
     */
     void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> refreshListener);

    /**
     * 结束下拉刷新
     */
     void onPullDownRefreshComplete();

    /**
     * 结束上拉加载更多
     */
     void onPullUpRefreshComplete();

    /**
     * 得到可刷新的View对象
     *
     * @return 返回调用{@link #createRefreshableView(Context, AttributeSet)} 方法返回的对象
     */
     T getRefreshableView();

    /**
     * 得到Header布局对象
     *
     * @return Header布局对象
     */
     LoadingLayout getHeaderLoadingLayout();

    /**
     * 得到Footer布局对象
     *
     * @return Footer布局对象
     */
     LoadingLayout getFooterLoadingLayout();

    /**
     * 设置最后更新的时间文本
     *
     * @param label 文本
     */
     void setLastUpdatedLabel(CharSequence label);
}
