package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.viewmodels.Idea;

/**
 * Created by pinyaoting on 11/26/16.
 */

public interface IdeaSearchInteractorInterface {

    int getResultCount();
    Idea getResultAtPos(int pos);
    void acceptResultAtPos(int pos);
    void search(String keyword);

}
