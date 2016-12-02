package com.doublesp.coherence.adapters;

import com.doublesp.coherence.fragments.GoalSearchFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.fragments.SavedIdeasFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by pinyaoting on 11/16/16.
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"Explore Recipes", "Create Grocery List", "Saved Ideas"};
    private Context mContext;
    private GoalSearchFragment mGoalSearchFragment;
    private ListCompositionFragment mListCompositionFragment;
    private SavedIdeasFragment mSavedIdeasFragment;

    public HomeFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mGoalSearchFragment = GoalSearchFragment.newInstance();
                return mGoalSearchFragment;
            case 1:
                mListCompositionFragment = ListCompositionFragment.newInstance();
                return mListCompositionFragment;
            default:
                mSavedIdeasFragment = SavedIdeasFragment.newInstance();
                return mSavedIdeasFragment;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

}
