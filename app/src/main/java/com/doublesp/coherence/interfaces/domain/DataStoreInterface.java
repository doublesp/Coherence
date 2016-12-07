package com.doublesp.coherence.interfaces.domain;

import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.GoalReducer;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaReducer;
import com.doublesp.coherence.viewmodels.Plan;

import java.util.List;

import rx.Observer;

public interface DataStoreInterface {

    void setIdeaState(ViewState state);

    void setSuggestionState(ViewState state);

    void setGoalState(ViewState state);

    void addIdea(Idea idea);

    void setIdeas(List<Idea> ideas);

    void moveIdeaToBottom(int pos);

    void moveIdeaToTop(int pos);

    void removeIdea(int pos);

    void clearIdeas();

    void setSuggestions(List<Idea> ideas);

    void setExploreGoals(List<Goal> goals);

    void setSavedGoals(List<Goal> goals);

    IdeaReducer getIdeaReducer(String id);

    GoalReducer getExploreGoalReducer(String id);

    GoalReducer getSavedGoalReducer(String id);

    void clearSuggestions();

    void clearGoals();

    Idea getIdeaAtPos(int pos);

    Idea getSuggestionAtPos(int pos);

    int getIdeaCount();

    int getSuggestionCount();

    void subscribeToIdeaStateChanges(Observer<ViewState> observer);

    void subscribeToSuggestionStateChanges(Observer<ViewState> observer);

    void subscribeToGoalStateChanges(Observer<ViewState> observer);

    Plan getPlan();

    void setPlan(Plan plan);

    Plan createPlan(String id);

    Goal getGoalAtPos(int pos);

    int getGoalCount();

    int getGoalFlag();

    void clearPlan();

    void setGoalFlag(int flag);

    void setPendingIdeas(String id, List<Idea> pendingIdeas);

    void loadPendingIdeas(String id);

    int getPendingIdeasCount(String id);

    Idea getPendingIdea(String id, int pos);
}
