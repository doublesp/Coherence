package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.fragments.IdeaPreviewFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;

/**
 * Created by pinyaoting on 11/11/16.
 */

public interface ListCompositionInjectorInterface {

    void inject(ListCompositionFragment fragment);
    void inject(IdeaPreviewFragment fragment);

}
