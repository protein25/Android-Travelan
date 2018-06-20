package travelan.art.sangeun.travelan.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import travelan.art.sangeun.travelan.InfoFragment;
import travelan.art.sangeun.travelan.NewspeedFragment;
import travelan.art.sangeun.travelan.PlanFragment;
import travelan.art.sangeun.travelan.SettingFragment;
import travelan.art.sangeun.travelan.utils.BaseFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter{
    List<BaseFragment> fragments = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);

        initFragments();
    }

    private void initFragments() {
        fragments.add(new NewspeedFragment());
        fragments.add(new PlanFragment());
        fragments.add(new InfoFragment());
        fragments.add(new SettingFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
