package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.fragments.GoalSearchFragment;
import com.doublesp.coherence.fragments.IdeaReviewFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.fragments.SavedGoalsFragment;

/**
 * Created by pinyaoting on 11/11/16.
 */

public interface InjectorInterface {

    void inject(ListCompositionFragment fragment);

    void inject(IdeaReviewFragment fragment);

    void inject(SavedGoalsFragment fragment);

    void inject(GoalSearchFragment fragment);

}
