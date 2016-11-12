package com.doublesp.coherence.dependencies.modules.presentation;

import com.doublesp.coherence.activities.ListCompositionActivity;
import com.doublesp.coherence.interfaces.presentation.ExploreFragmentActionHandlerInterface;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pinyaoting on 11/11/16.
 */

@Module
public class ListCompositionActivityModule {

    private final ListCompositionActivity mActivity;
    public ListCompositionActivityModule(ListCompositionActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PresentationLayerScope
    public ExploreFragmentActionHandlerInterface providesExploreFragmentActionHandler() {
        return null;
    }
}
