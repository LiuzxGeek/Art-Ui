package com.art.uilibrary.scroller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @Author liuxian
 * @Date 2017/8/10 13:58
 * @Des
 */

public class ScrollPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragments;
    private final List<String> mTitles;

    public ScrollPagerAdapter(FragmentManager fm, List<String> mTitles, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return mTitles.get(position);
    }

}
