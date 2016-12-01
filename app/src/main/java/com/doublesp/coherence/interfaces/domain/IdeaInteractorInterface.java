package com.doublesp.coherence.interfaces.domain;

import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface IdeaInteractorInterface {

    void addIdea(String content);

    void acceptSuggestedIdeaAtPos(int pos);

    void updateIdea(int pos, String content);

    void crossoutIdea(int pos);

    void uncrossoutIdea(int pos);

    void removeIdea(int pos);

    void clearIdeas();

    void getSuggestions(String keyword);

    void subscribeIdeaStateChange(Observer<ViewState> observer);

    void subscribeSuggestionStateChange(Observer<ViewState> observer);

    int getIdeaCount();

    int getSuggestionCount();

    Idea getIdeaAtPos(int pos);

    Plan getPlan();

    Plan createPlan(String id);

    void setPlan(Plan plan);

    void loadIdeasFromGoal(Goal goal);

}
