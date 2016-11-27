package com.doublesp.coherence.dependencies.components.presentation;

import com.doublesp.coherence.dependencies.modules.presentation.MainActivityModule;
import com.doublesp.coherence.fragments.IdeaReviewFragment;
import com.doublesp.coherence.fragments.IdeaSearchResultFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import dagger.Subcomponent;

/**
 * Created by pinyaoting on 11/11/16.
 */

@PresentationLayerScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivitySubComponent {
    void inject(ListCompositionFragment fragment);
    void inject(IdeaSearchResultFragment fragment);
    void inject(IdeaReviewFragment fragment);
}
