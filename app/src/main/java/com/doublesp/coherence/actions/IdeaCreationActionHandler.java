package com.doublesp.coherence.actions;

import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaActionHandlerInterface;

import android.text.Editable;

/**
 * Created by pinyaoting on 11/12/16.
 */

public class IdeaCreationActionHandler implements IdeaActionHandlerInterface {

    IdeaInteractorInterface mIdeaInteractor;

    public IdeaCreationActionHandler (IdeaInteractorInterface ideaInteractor) {
        mIdeaInteractor = ideaInteractor;
    }

    @Override
    public void afterTextChanged(Editable s) {
        final int i = s.length();
        if (i == 0) {
            return;
        }
        if (s.subSequence(i-1, i).toString().equals("\n")) {
            mIdeaInteractor.addIdea(s.toString().trim());
            s.clear();
        }
    }

    @Override
    public void onSuggestionClick(int pos) {
        mIdeaInteractor.acceptSuggestedIdeaAtPos(pos);
    }
}
