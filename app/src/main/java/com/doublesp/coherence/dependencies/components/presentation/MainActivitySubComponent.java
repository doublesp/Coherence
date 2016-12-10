package com.doublesp.coherence.dependencies.components.presentation;

import com.doublesp.coherence.activities.MainActivity;
import com.doublesp.coherence.dependencies.modules.presentation.MainActivityModule;
import com.doublesp.coherence.fragments.GoalDetailViewPagerFragment;
import com.doublesp.coherence.fragments.GoalPreviewFragment;
import com.doublesp.coherence.fragments.GoalSearchFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.fragments.SavedIdeasFragment;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import dagger.Subcomponent;

@PresentationLayerScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivitySubComponent {

    void inject(ListCompositionFragment fragment);

    void inject(GoalSearchFragment fragment);

    void inject(GoalPreviewFragment fragment);

    void inject(SavedIdeasFragment fragment);

    void inject(GoalDetailViewPagerFragment fragment);

    void inject(MainActivity activity);
}
