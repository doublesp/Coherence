package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.viewmodels.Goal;

/**
 * Created by pinyaoting on 11/28/16.
 */

public interface GoalDetailActionHandlerInterface {

    void onCreateIdeaListClick(int pos);
    void onBookmarkClick(int pos);

    interface ListCompositionDialogHandlerInterface {

        void compose(Goal goal);

    }
}
