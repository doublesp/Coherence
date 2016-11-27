package com.doublesp.coherence.actions;

import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaActionHandlerInterface;
import com.doublesp.coherence.viewmodels.Idea;

/**
 * Created by pinyaoting on 11/12/16.
 */

public class IdeaCreationActionHandler implements IdeaActionHandlerInterface {

    IdeaPreviewHandlerInterface mPreviewHandler;
    IdeaInteractorInterface mIdeaInteractor;

    public IdeaCreationActionHandler(IdeaPreviewHandlerInterface previewHandler,
            IdeaInteractorInterface ideaInteractor) {
        mPreviewHandler = previewHandler;
        mIdeaInteractor = ideaInteractor;
    }

    @Override
    public void onSuggestionClick(int pos) {
        mIdeaInteractor.acceptSuggestedIdeaAtPos(pos);
    }

    @Override
    public void onPreviewButtonClick(int pos) {
        Idea idea = mIdeaInteractor.getIdeaAtPos(pos);
        mPreviewHandler.showPreviewDialog(idea);
    }

    public interface IdeaPreviewHandlerInterface {
        void showPreviewDialog(Idea idea);
    }
}
