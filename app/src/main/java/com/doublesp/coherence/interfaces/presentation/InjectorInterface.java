package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.fragments.IdeaReviewFragment;
import com.doublesp.coherence.fragments.IdeaSearchResultFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;

/**
 * Created by pinyaoting on 11/11/16.
 */

public interface InjectorInterface {

    void inject(ListCompositionFragment fragment);
    void inject(IdeaSearchResultFragment fragment);
    void inject(IdeaReviewFragment fragment);

}
