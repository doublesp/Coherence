package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.viewmodels.Idea;

public interface IdeaViewHolderInterface {

    void setPosition(int position);

    void setViewModel(Idea viewModel);

    void setHandler(ListFragmentActionHandlerInterface handler);

    void executePendingBindings();

}
