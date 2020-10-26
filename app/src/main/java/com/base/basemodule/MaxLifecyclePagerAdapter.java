package com.base.basemodule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * create by lzs 2020-09-27 10:41
 * email:343067508@qq.com
 * description:
 **/
public class MaxLifecyclePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titles;


    public MaxLifecyclePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    public MaxLifecyclePagerAdapter(FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments == null ? null : fragments.get(position);
    }

    public void setData(List<Fragment> fragments, List<String> titles) {
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }
}
