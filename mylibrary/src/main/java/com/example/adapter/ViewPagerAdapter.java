package com.example.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> listView;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.listView = list;
    }

    @Override
    public int getCount() {
        return listView == null ? 0 : listView.size();
    }

    @Override
    public Fragment getItem(int position) {
        return listView.get(position);
    }
}
