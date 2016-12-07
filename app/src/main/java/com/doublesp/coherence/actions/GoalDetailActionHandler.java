package com.doublesp.coherence.actions;

import com.doublesp.coherence.interfaces.presentation.GoalDetailActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.viewmodels.Goal;

public class GoalDetailActionHandler implements GoalDetailActionHandlerInterface {

    ListCompositionDialogHandlerInterface mDialogHandler;
    GoalInteractorInterface mInteractor;

    public GoalDetailActionHandler(
            ListCompositionDialogHandlerInterface dialogHandler,
            GoalInteractorInterface interactor) {
        mDialogHandler = dialogHandler;
        mInteractor = interactor;
    }

    @Override
    public void onCreateIdeaListClick(int pos) {
        Goal goal = mInteractor.getGoalAtPos(pos);
        mDialogHandler.compose(goal);
    }

    @Override
    public void onBookmarkClick(int pos) {
        mInteractor.bookmarkGoalAtPos(pos);
    }

}
