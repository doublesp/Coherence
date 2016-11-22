package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.fragments.IdeaReviewFragment;

/**
 * Created by pinyaoting on 11/11/16.
 */

public interface HomeInjectorInterface {

    void inject(ListCompositionFragment fragment);

    void inject(IdeaReviewFragment fragment);

}
