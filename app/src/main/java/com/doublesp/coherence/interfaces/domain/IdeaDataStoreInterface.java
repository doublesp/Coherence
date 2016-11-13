package com.doublesp.coherence.interfaces.domain;

import com.doublesp.coherence.viewmodels.Idea;

import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface IdeaDataStoreInterface {
    void addIdea(Idea idea);

    void addIdeas(List<Idea> idea);

    void crossoutIdea(Idea idea);

    void uncrossoutIdea(Idea idea);

    void removeIdea(Idea idea);

    void setSuggestions(List<Idea> ideas);

    Idea getIdeaAtPos(int pos);

    int getIdeaCount();

    void subscribeToIdeaListChanges(Observer<List<Idea>> observer);
}
