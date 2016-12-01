package com.doublesp.coherence.interfaces.domain;

import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface DataStoreInterface {

    void setIdeaState(ViewState state);

    void setSuggestionState(ViewState state);

    void setGoalState(ViewState state);

    void setSavedGoalState(ViewState state);

    void addIdea(Idea idea);

    void setIdeas(List<Idea> ideas);

    void updateIdea(int pos, Idea idea);

    void removeIdea(int pos);

    void clearIdeas();

    void setSuggestions(List<Idea> ideas);

    void setGoals(List<Goal> goals);

    void setSavedGoals(List<Goal> goals);

    void updateGoal(int pos, Goal goal);

    void updateSavedGoal(int pos, Goal goal);

    void clearSuggestions();

    void clearGoals();

    Idea getIdeaAtPos(int pos);

    Idea getSuggestionAtPos(int pos);

    Goal getGoalAtPos(int pos);

    Goal getSavedGoalAtPos(int pos);

    int getIdeaCount();

    int getSuggestionCount();

    int getGoalCount();

    int getSavedGoalCount();

    void subscribeToIdeaStateChanges(Observer<ViewState> observer);

    void subscribeToSuggestionStateChanges(Observer<ViewState> observer);

    void subscribeToGoalStateChanges(Observer<ViewState> observer);

    void subscribeToSavedGoalStateChanges(Observer<ViewState> observer);

    Plan getPlan();

    void setPlan(Plan plan);

    Plan createPlan(String id);
}
