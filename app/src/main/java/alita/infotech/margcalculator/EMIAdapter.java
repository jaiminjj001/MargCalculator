package alita.infotech.margcalculator;

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
        else if(position == 2)
            return new RFCompare();
        else
            return null;
    }

    public String getPageTitle(int position){
        if(position == 0)
            return "Reducing Interest";
        else if(position == 1)
            return "Flat Interest";
        else if(position == 2)
            return "RFCompare";
        else
            return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
