package com.doublesp.coherence.dependencies.modules.presentation;

import com.doublesp.coherence.actions.ExploreFragmentActionHandler;
import com.doublesp.coherence.activities.ExploreActivity;
import com.doublesp.coherence.interfaces.presentation.ExploreFragmentActionHandlerInterface;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pinyaoting on 11/11/16.
 */

@Module
public class ExploreActivityModule {

    private final ExploreActivity mActivity;
    public ExploreActivityModule(ExploreActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PresentationLayerScope
    public ExploreFragmentActionHandlerInterface providesExploreFragmentActionHandler() {
        return new ExploreFragmentActionHandler(mActivity);
    }

}
