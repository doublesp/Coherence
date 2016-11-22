package com.doublesp.coherence.dependencies.modules.presentation;

import com.doublesp.coherence.actions.IdeaCreationActionHandler;
import com.doublesp.coherence.actions.ListFragmentActionHandler;
import com.doublesp.coherence.activities.HomeActivity;
import com.doublesp.coherence.adapters.IdeaSelectorArrayAdapter;
import com.doublesp.coherence.adapters.ListCompositionArrayAdapter;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import android.support.v7.widget.RecyclerView;

import java.util.Map;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pinyaoting on 11/11/16.
 */

@Module
public class HomeActivityModule {

    private final HomeActivity mActivity;
    private final int mCategory;

    public HomeActivityModule(HomeActivity activity, int category) {
        mActivity = activity;
        mCategory = category;
    }

    @Provides
    @PresentationLayerScope
    @Named("Composition")
    public RecyclerView.Adapter<RecyclerView.ViewHolder> providesListCompositionArrayAdapter(
            IdeaInteractorInterface ideaInteractor, IdeaActionHandlerInterface ideaActionHandler) {
        return new ListCompositionArrayAdapter(ideaInteractor, ideaActionHandler);
    }

    @Provides
    @PresentationLayerScope
    public IdeaActionHandlerInterface providesIdeaActionHandler(
            IdeaInteractorInterface ideaInteractor) {
        return new IdeaCreationActionHandler(mActivity, ideaInteractor);
    }

    @Provides
    @PresentationLayerScope
    public ListFragmentActionHandlerInterface providesListFragmentActionHandler(
            IdeaInteractorInterface ideaInteractor) {
        return new ListFragmentActionHandler(mActivity, ideaInteractor);
    }

    @Provides
    @PresentationLayerScope
    public IdeaInteractorInterface providesIdeaInteractor(
            Map<Integer, IdeaInteractorInterface> ideaInteractors) {
        IdeaInteractorInterface ideaInteractor = ideaInteractors.get(mCategory);
        return ideaInteractor;
    }

    @Provides
    @PresentationLayerScope
    @Named("Preview")
    public RecyclerView.Adapter<RecyclerView.ViewHolder> providesIdeaPreviewArrayAdapter(
            IdeaInteractorInterface ideaInteractor) {
        return new IdeaSelectorArrayAdapter(ideaInteractor);
    }

}
