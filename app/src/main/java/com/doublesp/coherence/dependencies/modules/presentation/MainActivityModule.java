package com.doublesp.coherence.dependencies.modules.presentation;

import android.support.v7.widget.RecyclerView;

import com.doublesp.coherence.actions.GoalActionHandler;
import com.doublesp.coherence.actions.GoalDetailActionHandler;
import com.doublesp.coherence.actions.ListFragmentActionHandler;
import com.doublesp.coherence.actions.SavedGoalActionHandler;
import com.doublesp.coherence.activities.MainActivity;
import com.doublesp.coherence.adapters.GoalArrayAdapter;
import com.doublesp.coherence.adapters.ListCompositionArrayAdapter;
import com.doublesp.coherence.adapters.SavedGoalArrayAdapter;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalDetailActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.SavedIdeasActionHandlerInterface;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import java.util.Map;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pinyaoting on 11/11/16.
 */

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
            GoalInteractorInterface interactor,
            @Named("GoalAction") GoalActionHandlerInterface actionHandler) {
        return new GoalArrayAdapter(interactor, actionHandler);
    }

    @Provides
    @PresentationLayerScope
    @Named("SavedGoal")
    public RecyclerView.Adapter<RecyclerView.ViewHolder> providesBookmarkGoalArrayAdapter(
            GoalInteractorInterface interactor,
            @Named("SavedGoalAction") GoalActionHandlerInterface actionHandler) {
        return new SavedGoalArrayAdapter(interactor, actionHandler);
    }

    @Provides
    @PresentationLayerScope
    @Named("GoalAction")
    public GoalActionHandlerInterface providesGoalActionHandler(
            GoalInteractorInterface interactor) {
        return new GoalActionHandler(mActivity, interactor);
    }

    @Provides
    @PresentationLayerScope
    @Named("SavedGoalAction")
    public GoalActionHandlerInterface providesSavedGoalActionHandler(
            GoalInteractorInterface interactor) {
        return new SavedGoalActionHandler(mActivity, interactor);
    }

    @Provides
    @PresentationLayerScope
    public GoalDetailActionHandlerInterface providesGoalDetailActionHandler() {
        return new GoalDetailActionHandler(mActivity);
    }

    @Provides
    @PresentationLayerScope
    public SavedIdeasActionHandlerInterface providesSavedIdeasActionHandler() {
        return mActivity;
    }
}
