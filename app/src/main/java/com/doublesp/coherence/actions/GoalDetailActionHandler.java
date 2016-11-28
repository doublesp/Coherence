package com.doublesp.coherence.actions;

import com.doublesp.coherence.interfaces.presentation.GoalDetailActionHandlerInterface;
import com.doublesp.coherence.viewmodels.Goal;

/**
 * Created by pinyaoting on 11/27/16.
 */

public class GoalDetailActionHandler implements GoalDetailActionHandlerInterface {

    ListCompositionDialogHandlerInterface mDialogHandler;

    public GoalDetailActionHandler(ListCompositionDialogHandlerInterface dialogHandler) {
        mDialogHandler = dialogHandler;
    }

    @Override
    public void onCreateIdeaListClick(Goal goal) {
        mDialogHandler.showListCompositionDialog(goal);
    }

}
