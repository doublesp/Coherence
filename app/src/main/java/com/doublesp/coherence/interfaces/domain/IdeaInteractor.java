package com.doublesp.coherence.interfaces.domain;

import com.doublesp.coherence.viewmodels.Idea;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface IdeaInteractor {

    void createIdea(Idea idea);
    void crossoutIdea(Idea idea);
    void uncrossoutIdea(Idea idea);
    void removeIdea(Idea idea);
    void getRelatedIdeas(Idea idea);
    void subscribe(Observer observer);

}
