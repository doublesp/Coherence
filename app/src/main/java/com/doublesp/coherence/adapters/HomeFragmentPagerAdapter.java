package com.doublesp.coherence.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doublesp.coherence.R;
import com.doublesp.coherence.fragments.GoalSearchFragment;
import com.doublesp.coherence.fragments.SavedIdeasFragment;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    public static final int SEARCH_GOAL = 0;
    public static final int SAVED_GOALS = 1;
    public static final int SAVED_IDEAS = 2;

    private String tabTitles[] =
            new String[]{"Explore Goals", "Saved Goals", "Saved Ideas"};
    private Context mContext;
    private GoalSearchFragment mGoalSearchFragment;
    private GoalSearchFragment mSavedGoalFragment;
    private SavedIdeasFragment mSavedIdeasFragment;

    public HomeFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case SEARCH_GOAL:
                return getGoalSearchFragment();
            case SAVED_GOALS:
                return getSavedGoalFragment();
            case SAVED_IDEAS:
                return getSavedIdeasFragment();
            default:
                return null;
        }
    }

    public GoalSearchFragment getGoalSearchFragment() {
        if (mGoalSearchFragment == null) {
            mGoalSearchFragment = GoalSearchFragment.newInstance();
        }
        return mGoalSearchFragment;
    }

    public GoalSearchFragment getSavedGoalFragment() {
        if (mSavedGoalFragment == null) {
            mSavedGoalFragment = GoalSearchFragment.newInstance();
        }
        return mSavedGoalFragment;
    }

    public SavedIdeasFragment getSavedIdeasFragment() {
        if (mSavedIdeasFragment == null) {
            mSavedIdeasFragment = SavedIdeasFragment.newInstance();
        }
        return mSavedIdeasFragment;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case SEARCH_GOAL:
                title = mContext.getString(R.string.explore_goals);
                break;
            case SAVED_GOALS:
                title = mContext.getString(R.string.saved_goals);
                break;
            case SAVED_IDEAS:
                title = mContext.getString(R.string.saved_ideas);
                break;
            default:
        }
        return title;
    }
}
