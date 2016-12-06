package com.doublesp.coherence.actions;

import android.text.Editable;

import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;

public class GoalActionHandler implements GoalActionHandlerInterface {

    PreviewHandlerInterface mPreviewHandler;
    GoalInteractorInterface mInteractor;

    public GoalActionHandler(PreviewHandlerInterface previewHandler,
            GoalInteractorInterface interactor) {
        mPreviewHandler = previewHandler;
        mInteractor = interactor;
    }

    @Override
    public void afterTextChanged(Editable s) {
        final int i = s.length();
        if (i == 0) {
            return;
        }
        if (s.subSequence(i - 1, i).toString().equals("\n")) {
            mInteractor.search(s.toString().trim());
            s.clear();
        } else {
            mInteractor.search(s.toString().trim());
        }
    }

    @Override
    public void onBookmarkClick(int pos) {
        mInteractor.bookmarkGoalAtPos(pos);
    }

    @Override
    public void onPreviewButtonClick(int pos) {
        mPreviewHandler.showPreviewDialog(pos);
    }

}
