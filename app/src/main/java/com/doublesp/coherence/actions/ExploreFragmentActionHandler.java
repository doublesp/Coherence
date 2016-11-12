package com.doublesp.coherence.actions;

import android.util.Log;

import com.doublesp.coherence.R;
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
        switch (category) {
            case R.id.idea_category_travel:
            case R.id.idea_category_groceries:
            case R.id.idea_category_movies:
                Log.d("INFO", "tapped");
                // TODO: either start new intent or present dialog fragment from ExploreActivity
                break;
        }
    }
}
