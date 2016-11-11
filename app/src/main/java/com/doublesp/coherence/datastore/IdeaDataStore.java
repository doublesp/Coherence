package com.doublesp.coherence.datastore;

import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.viewmodels.Idea;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.subjects.PublishSubject;

/**
 * Created by pinyaoting on 11/10/16.
 */

public class IdeaDataStore implements IdeaDataStoreInterface {

    List<Idea> mIdeas;
    PublishSubject<List<Idea>> mPublisher;

    public IdeaDataStore() {
        mIdeas = new ArrayList<>();
        mPublisher = PublishSubject.create();
    }

    @Override
    public void addIdea(Idea idea) {
        mIdeas.add(idea);
        mPublisher.onCompleted();
    }

    @Override
    public void addIdeas(List<Idea> idea) {
        mIdeas.addAll(idea);
        mPublisher.onCompleted();
    }

    @Override
    public void crossoutIdea(Idea idea) {
        mIdeas.set(mIdeas.indexOf(idea), new Idea(idea.getId(), idea.getCategory(), idea.getContent(), true));
        mPublisher.onCompleted();
    }

    @Override
    public void uncrossoutIdea(Idea idea) {
        mIdeas.set(mIdeas.indexOf(idea), new Idea(idea.getId(), idea.getCategory(), idea.getContent(), false));
        mPublisher.onCompleted();
    }

    @Override
    public void removeIdea(Idea idea) {
        mIdeas.remove(idea);
        mPublisher.onCompleted();
    }

    @Override
    public void subscribeToIdeaListChanges(Observer<List<Idea>> observer) {
        mPublisher.subscribe(observer);
    }

    @Override
    public Idea getIdeaAtPos(int pos) {
        return mIdeas.get(pos);
    }
}
