package com.doublesp.coherence.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doublesp.coherence.fragments.GoalPreviewFragment;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pinyaoting on 12/6/16.
 */

public class GoalPreviewFragmentPagerAdapter extends FragmentPagerAdapter {

    GoalInteractorInterface mGoalInteractor;
    Map<Integer, GoalPreviewFragment> mFragments;

    public GoalPreviewFragmentPagerAdapter(FragmentManager fm, GoalInteractorInterface goalInteractor) {
        super(fm);
        mGoalInteractor = goalInteractor;
        mFragments = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments.containsKey(position)) {
            return mFragments.get(position);
        }
        GoalPreviewFragment goalPreviewFragment = GoalPreviewFragment.newInstance(position);
        mFragments.put(position, goalPreviewFragment);
        return goalPreviewFragment;
    }

    @Override
    public int getCount() {
        return mGoalInteractor.getGoalCount();
    }
}
