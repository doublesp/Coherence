package com.doublesp.coherence.adapters;

import com.doublesp.coherence.fragments.ExploreFragment;
import com.doublesp.coherence.fragments.MapFragment;
import com.doublesp.coherence.fragments.PlanFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by pinyaoting on 11/16/16.
 */

public class ExploreFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "New Plan", "My Plans", "Shared with me", "Map" };
    private Context mContext;

    public ExploreFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ExploreFragment.newInstance();
            case 3:
                return MapFragment.newInstance();
            default:
                return PlanFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

}
