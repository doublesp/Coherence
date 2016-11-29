package com.doublesp.coherence.interfaces.domain;

import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface DataStoreInterface {

    void setIdeaState(int state);

    void setSuggestionState(int state);

    void setGoalState(int state);

    void setSavedGoalState(int state);

    void addIdea(Idea idea);

    void setIdeas(List<Idea> ideas);

    void updateIdea(int pos, Idea idea);

    void removeIdea(int pos);

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

    void subscribeToIdeaStateChanges(Observer<Integer> observer);

    void subscribeToSuggestionStateChanges(Observer<Integer> observer);

    void subscribeToGoalStateChanges(Observer<Integer> observer);

    void subscribeToSavedGoalStateChanges(Observer<Integer> observer);

    Plan getPlan();

    Plan createPlan(String id);
}
