package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.fragments.GoalDetailViewPagerFragment;
import com.doublesp.coherence.fragments.GoalPreviewFragment;
import com.doublesp.coherence.fragments.GoalSearchFragment;
import com.doublesp.coherence.fragments.IdeaReviewFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.fragments.SavedIdeasFragment;

public interface InjectorInterface {

    void inject(ListCompositionFragment fragment);

    void inject(IdeaReviewFragment fragment);

    void inject(GoalSearchFragment fragment);

    void inject(GoalPreviewFragment fragment);

    void inject(SavedIdeasFragment fragment);

    void inject(GoalDetailViewPagerFragment fragment);
}
