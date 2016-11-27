package com.doublesp.coherence.interfaces.domain;

import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface IdeaDataStoreInterface {

    void setIdeaState(int state);

    void setSuggestionState(int state);

    void addIdea(Idea idea);

    void updateIdea(int pos, Idea idea);

    void removeIdea(int pos);

    void setSuggestions(List<Idea> ideas);

    void clearSuggestions();

    Idea getIdeaAtPos(int pos);

    Idea getSuggestionAtPos(int pos);

    int getIdeaCount();

    int getSuggestionCount();

    void subscribeToIdeaStateChanges(Observer<Integer> observer);

    void subscribeToSuggestionStateChanges(Observer<Integer> observer);

    Plan getPlan();
}
