package com.doublesp.coherence.dependencies.components.presentation;

import com.doublesp.coherence.dependencies.modules.presentation.ListCompositionActivityModule;
import com.doublesp.coherence.fragments.IdeaPreviewFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import dagger.Subcomponent;

/**
 * Created by pinyaoting on 11/11/16.
 */

@PresentationLayerScope
@Subcomponent(modules = ListCompositionActivityModule.class)
public interface ListCompositionActivitySubComponent {
    void inject(ListCompositionFragment fragment);
    void inject(IdeaPreviewFragment fragment);
}
