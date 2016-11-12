package com.doublesp.coherence.dependencies.components.presentation;

import com.doublesp.coherence.dependencies.modules.presentation.ExploreActivityModule;
import com.doublesp.coherence.fragments.ExploreFragment;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import dagger.Subcomponent;

/**
 * Created by pinyaoting on 11/11/16.
 */

@PresentationLayerScope
@Subcomponent(modules = ExploreActivityModule.class)
public interface ExploreActivitySubComponent {

    void inject(ExploreFragment fragment);

}
