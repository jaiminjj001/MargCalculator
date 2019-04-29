package com.example.margcalculator;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class EMIAdapter extends FragmentPagerAdapter {

    public EMIAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return new ReducingInterest();
        else if(position == 1)
            return new FlatInterest();
        else
            return null;
    }

    public String getPageTitle(int position){
        if(position == 0)
            return "Reducing Interest";
        else if(position == 1)
            return "Flat Interest";
        else
            return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
