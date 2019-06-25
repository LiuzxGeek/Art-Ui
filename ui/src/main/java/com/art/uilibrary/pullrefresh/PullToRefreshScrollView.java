package com.art.uilibrary.pullrefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.art.uilibrary.widget.AutoScrollView;

/**
 * 封装了ScrollView的下拉刷新
 * /**********************************************************
 * ********************* Copyright 2011, 2012 Chris Banes.
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
@SuppressLint("NewApi")
public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView> {
    /**
     * Wrong state class, expecting View State but received class
     * android.widget.ScrollView$SavedState instead. This usually happens when
     * two views of different type have the same id in the same hierarchy. This
     * view's id is id/ptr_sv. Make sure other views do not use the same id.
     */
    private int stateToSave;

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshScrollView(Context context) {
        this(context, null);
        setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);// 兼容魅族手机下拉功能
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public PullToRefreshScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
    }

    /**
     * 构造方法
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
    }

    /**
     * android.util.AttributeSet)
     */
    @Override
    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        return new AutoScrollView(context, attrs);
    }

    /**
     */
    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }

    /**
     */
    @Override
    protected boolean isReadyForPullUp() {
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (scrollViewChild != null) {
            int childHeight = scrollViewChild.getHeight();
            int viewHeight = getHeight();
            int scrollY = mRefreshableView.getScrollY();
            int differHeight = childHeight - viewHeight;
            int result = scrollY - differHeight;
            return result >= 0;
        }
        return false;
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (getFooterLoadingLayout() != null)
                getFooterLoadingLayout().setState(ILoadingLayout.State.NO_MORE_DATA);

        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        // end
        ss.stateToSave = this.stateToSave;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(final Parcelable state) {
        // begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            try {
                super.onRestoreInstanceState(state);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        // end
        this.stateToSave = ss.stateToSave;
    }

    static class SavedState extends BaseSavedState {
        // required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }
    }
}
