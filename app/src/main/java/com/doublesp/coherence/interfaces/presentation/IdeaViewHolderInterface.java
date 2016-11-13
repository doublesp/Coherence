package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.viewmodels.Idea;

/**
 * Created by pinyaoting on 11/12/16.
 */

public interface IdeaViewHolderInterface {

    void setViewModel(Idea viewModel);
    void executePendingBindings();

}
