package com.doublesp.coherence.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doublesp.coherence.R;
import com.doublesp.coherence.fragments.GoalSearchFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.fragments.SavedIdeasFragment;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    public static final int SEARCH_GOAL = 0;
    public static final int CREATE_LIST = 1;
    public static final int SAVED_IDEAS = 2;

    private String tabTitles[] =
            new String[]{"Explore Recipes", "Create Grocery List", "Saved Ideas"};
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
            case SEARCH_GOAL:
                mGoalSearchFragment = GoalSearchFragment.newInstance();
                return mGoalSearchFragment;
            case CREATE_LIST:
                mListCompositionFragment = ListCompositionFragment.newInstance();
                return mListCompositionFragment;
            case SAVED_IDEAS:
                mSavedIdeasFragment = SavedIdeasFragment.newInstance();
                return mSavedIdeasFragment;
            default:
                return null;
        }
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
                title = mContext.getString(R.string.app_name);
                break;
            case CREATE_LIST:
                title = mContext.getString(R.string.create_grocery_list);
                break;
            case SAVED_IDEAS:
                title = mContext.getString(R.string.saved_grocery_list);
                break;
            default:
        }
        return title;
    }
}
