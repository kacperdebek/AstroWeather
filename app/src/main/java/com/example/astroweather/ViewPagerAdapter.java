package com.example.astroweather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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

    public String getFragmentTag(int pos){
        return "android:switcher:"+R.id.fragmentsPager+":"+pos;
    }

}
