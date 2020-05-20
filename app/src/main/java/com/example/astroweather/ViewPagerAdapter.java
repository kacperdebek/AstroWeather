package com.example.astroweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int simultaneousPages;
    ViewPagerAdapter(FragmentManager fm, int simultaneousPages) {
        super(fm);
        this.simultaneousPages = simultaneousPages;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SunFragment();
            case 1:
                return new MoonFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public float getPageWidth(int position) {
        return(1f/simultaneousPages);
    }
}
