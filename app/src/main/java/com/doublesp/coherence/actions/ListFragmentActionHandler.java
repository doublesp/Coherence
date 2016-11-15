package com.doublesp.coherence.actions;

import com.doublesp.coherence.activities.ListCompositionActivity;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;

/**
 * Created by pinyaoting on 11/13/16.
 */

public class ListFragmentActionHandler implements ListFragmentActionHandlerInterface {

    ListCompositionActivity mActivity;

    public ListFragmentActionHandler(ListCompositionActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onFloatingAcitonButtonClick() {
        mActivity.showPreviewDialog();
    }
}
