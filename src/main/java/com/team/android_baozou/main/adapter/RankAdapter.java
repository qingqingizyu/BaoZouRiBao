package com.team.android_baozou.main.adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by SoSho on 2016/10/12.
 */

public class RankAdapter extends FragmentStatePagerAdapter {
    private String[] mArr;

    private List<Fragment> mFragments;

    public RankAdapter(FragmentManager fm, List<Fragment> fragments, String[] arr) {
        super(fm);
        this.mFragments = fragments;
        this.mArr = arr;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mArr.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mArr[position];
    }
}
