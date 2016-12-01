package com.doublesp.coherence.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doublesp.coherence.fragments.GoalSearchFragment;
import com.doublesp.coherence.fragments.SavedIdeasFragment;

/**
 * Created by pinyaoting on 11/16/16.
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"Explore Recipes", "Saved Ideas"};
    private Context mContext;

    public HomeFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return GoalSearchFragment.newInstance();
            default:
                return SavedIdeasFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

}
