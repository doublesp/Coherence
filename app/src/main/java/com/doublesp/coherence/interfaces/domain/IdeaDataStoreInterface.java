package com.doublesp.coherence.interfaces.domain;

import com.doublesp.coherence.viewmodels.Idea;

import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface IdeaDataStoreInterface {

    void setIdeaState(int state);

    void addIdea(Idea idea);

    void updateIdea(int pos, Idea idea);

    void removeIdea(int pos);

    void setSuggestions(List<Idea> ideas);

    Idea getIdeaAtPos(int pos);

    int getIdeaCount();

    void subscribeToIdeaStateChanges(Observer<Integer> observer);
}
