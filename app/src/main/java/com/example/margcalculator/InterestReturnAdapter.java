package com.example.margcalculator;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class InterestReturnAdapter  extends FragmentPagerAdapter {

    public InterestReturnAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return new FixedDeposits();
        else if(position == 1)
            return new RecurringDeposits();
        else if(position == 2)
            return new FdRdCompare();
        else
            return null;
    }

    public String getPageTitle(int position){
        if(position == 0)
            return "Fixed Deposits";
        else if(position == 1)
            return "Recurring Deposits";
        else if(position == 2)
            return "FdRdCompare";
        else
            return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
