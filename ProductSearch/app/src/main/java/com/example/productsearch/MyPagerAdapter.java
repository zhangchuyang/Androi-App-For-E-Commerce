package com.example.productsearch;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> titles;
    private List<Fragment> mFragment;
    private List<Integer> mIcons;


    public MyPagerAdapter(FragmentManager fm, List<String> mDatas, List<Fragment> fragments, List<Integer> iconList) {
        super(fm);
        this.titles = mDatas;
        this.mFragment = fragments;
        this.mIcons = iconList;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragment.get(i);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position % mFragment.size());
    }
}
