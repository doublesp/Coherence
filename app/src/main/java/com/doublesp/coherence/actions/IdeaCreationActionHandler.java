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

    public void afterTextChanged(Editable s, int pos) {
        final int i = s.length();
        if (i == 0) {
            return;
        }
        if (s.subSequence(i-1, i).toString().equals("\n")) {
            final String r = new StringBuilder(s.subSequence(0, i)).toString();
            mIdeaInteractor.addIdea(r.trim());
            s.clear();
        }
    }

}
