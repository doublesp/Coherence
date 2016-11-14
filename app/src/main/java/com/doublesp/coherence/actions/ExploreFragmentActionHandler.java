package com.doublesp.coherence.actions;

import com.doublesp.coherence.activities.ExploreActivity;
import com.doublesp.coherence.interfaces.presentation.ExploreFragmentActionHandlerInterface;

/**
 * Created by pinyaoting on 11/11/16.
 */

public class ExploreFragmentActionHandler implements ExploreFragmentActionHandlerInterface {

    ExploreActivity mActivity;

    public ExploreFragmentActionHandler(ExploreActivity activity) {
        mActivity = activity;
    }

    public void onCategorySelected(int category) {
        mActivity.startListCompositionActivity(category);
    }
}
