package com.art.uilibrary.base;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liuxian
 * @Date 2017/9/12 10:37
 * @Des
 */

public class PageViewAdapter extends PagerAdapter {
    private List<View> images;

    public PageViewAdapter() {
        this.images = new ArrayList<>();
    }

    public void invalidateData(List<View> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        container.removeView(images.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View mImageView = images.get(position % images.size());
        container.addView(mImageView);
        return mImageView;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}

