package com.doublesp.coherence.interfaces.domain;

import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import rx.Observer;

public interface IdeaInteractorInterface {

    void acceptSuggestedIdeaAtPos(int pos);

    void crossoutIdea(int pos);

    void uncrossoutIdea(int pos);

    void removeIdea(int pos);

    void getSuggestions(String keyword);

    void subscribeIdeaStateChange(Observer<ViewState> observer);

    void subscribeSuggestionStateChange(Observer<ViewState> observer);

    int getIdeaCount();

    int getSuggestionCount();

    Idea getIdeaAtPos(int pos);

    Idea getSuggestionAtPos(int pos);

    Plan getPlan();

    void setPlan(Plan plan);

    Plan createPlan(String id);

    void loadIdeasFromGoal(Goal goal);

    void discardPlanIfEmpty();

    void clearPlan();

}
