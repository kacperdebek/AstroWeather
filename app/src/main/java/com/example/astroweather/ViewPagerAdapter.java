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
            case 2:
                return new MainWeatherFragment();
            case 3:
                return new AdditionalWeatherFragment();
            case 4:
                return new FutureForecastFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public float getPageWidth(int position) {
        return(1f/simultaneousPages);
    }
}
