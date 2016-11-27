package com.doublesp.coherence.actions;

import com.doublesp.coherence.interfaces.presentation.IdeaSearchActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaSearchInteractorInterface;

import android.content.Context;
import android.text.Editable;

/**
 * Created by pinyaoting on 11/26/16.
 */

public class IdeaSearchActionHandler implements IdeaSearchActionHandlerInterface {

    Context mContext;
    IdeaSearchInteractorInterface mSearchInteractor;

    public IdeaSearchActionHandler(Context context, IdeaSearchInteractorInterface searchInteractor) {
        mContext = context;
        mSearchInteractor = searchInteractor;
    }

    @Override
    public void afterTextChanged(Editable s) {
        final int i = s.length();
        if (i == 0) {
            return;
        }
        if (s.subSequence(i - 1, i).toString().equals("\n")) {
            mSearchInteractor.search(s.toString().trim());
            s.clear();
        } else {
            mSearchInteractor.search(s.toString().trim());
        }
    }

}
