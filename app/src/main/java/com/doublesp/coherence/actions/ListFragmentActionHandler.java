package com.doublesp.coherence.actions;

import com.doublesp.coherence.activities.HomeActivity;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;

/**
 * Created by pinyaoting on 11/13/16.
 */

public class ListFragmentActionHandler implements ListFragmentActionHandlerInterface {

    HomeActivity mActivity;

    public ListFragmentActionHandler(HomeActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onFloatingAcitonButtonClick() {
        mActivity.showPreviewDialog();
    }
}
