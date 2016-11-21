package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.viewmodels.Idea;

/**
 * Created by pinyaoting on 11/12/16.
 */

public interface IdeaViewHolderInterface {

    void setPosition(int position);

    void setViewModel(Idea viewModel);

    void setHandler(IdeaActionHandlerInterface handler);

    void executePendingBindings();

}
