package com.doublesp.coherence.adapters;

import com.doublesp.coherence.fragments.SavedPlansFragment;
import com.doublesp.coherence.fragments.SavedIdeasFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by pinyaoting on 11/16/16.
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"Saved Ideas", "Saved Recipes"};
    private Context mContext;

    public HomeFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SavedIdeasFragment.newInstance();
            default:
                return SavedPlansFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

}
