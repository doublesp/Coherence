package com.doublesp.coherence.dependencies.modules.presentation;

import com.doublesp.coherence.actions.GoalActionHandler;
import com.doublesp.coherence.actions.GoalDetailActionHandler;
import com.doublesp.coherence.actions.ListFragmentActionHandler;
import com.doublesp.coherence.activities.MainActivity;
import com.doublesp.coherence.adapters.GoalArrayAdapter;
import com.doublesp.coherence.adapters.IdeasArrayAdapter;
import com.doublesp.coherence.adapters.ListCompositionArrayAdapter;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalDetailActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.SavedIdeasActionHandlerInterface;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import android.support.v7.widget.RecyclerView;

import java.util.Map;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    private final MainActivity mActivity;
    private final int mCategory;

    public MainActivityModule(MainActivity activity, int category) {
        mActivity = activity;
        mCategory = category;
    }

    @Provides
    @PresentationLayerScope
    @Named("Composition")
    public RecyclerView.Adapter<RecyclerView.ViewHolder> providesListCompositionArrayAdapter(
            IdeaInteractorInterface ideaInteractor,
            ListFragmentActionHandlerInterface ideaActionHandler) {
        return new ListCompositionArrayAdapter(ideaInteractor, ideaActionHandler);
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
    @Named("Goal")
    public RecyclerView.Adapter<RecyclerView.ViewHolder> providesGoalArrayAdapter(
            GoalInteractorInterface interactor, GoalActionHandlerInterface actionHandler) {
        return new GoalArrayAdapter(interactor, actionHandler);
    }

    @Provides
    @PresentationLayerScope
    public GoalActionHandlerInterface providesGoalActionHandler(
            GoalInteractorInterface interactor) {
        return new GoalActionHandler(mActivity, interactor);
    }

    @Provides
    @PresentationLayerScope
    public GoalDetailActionHandlerInterface providesGoalDetailActionHandler(
            GoalInteractorInterface interactor) {
        return new GoalDetailActionHandler(mActivity, interactor);
    }

    @Provides
    @PresentationLayerScope
    public SavedIdeasActionHandlerInterface providesSavedIdeasActionHandler() {
        return mActivity;
    }

    @Provides
    @PresentationLayerScope
    public IdeasArrayAdapter providesIdeasArrayAdapter(IdeaInteractorInterface ideaInteractor) {
        return new IdeasArrayAdapter(ideaInteractor);
    }
}
