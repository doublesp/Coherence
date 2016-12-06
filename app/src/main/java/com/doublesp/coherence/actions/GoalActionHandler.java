package com.doublesp.coherence.actions;

import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;

import android.content.Context;

public class GoalActionHandler implements GoalActionHandlerInterface {

    PreviewHandlerInterface mPreviewHandler;
    GoalInteractorInterface mInteractor;

    public GoalActionHandler(Context context,
                             GoalInteractorInterface interactor) {
        if (context instanceof PreviewHandlerInterface) {
            mPreviewHandler = (PreviewHandlerInterface) context;
        }
        mInteractor = interactor;
    }

    @Override
    public void onPreviewButtonClick(int pos) {
        mPreviewHandler.preview(pos);
    }

}
