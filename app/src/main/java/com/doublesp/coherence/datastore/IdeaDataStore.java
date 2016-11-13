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
    List<Idea> mBlankIdeas;
    List<Idea> mIdeaSuggestions;
    PublishSubject<List<Idea>> mPublisher;

    public IdeaDataStore() {
        mIdeas = new ArrayList<>();
        mIdeas.add(Idea.newInstanceOfUserGeneratedIdea());
        mBlankIdeas = new ArrayList<>();
        mBlankIdeas.add(Idea.newInstanceOfBlankIdea());
        mIdeaSuggestions = new ArrayList<>();
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
        mIdeas.set(mIdeas.indexOf(idea),
                new Idea(idea.getId(), idea.getCategory(), idea.getContent(), true, idea.getType(), idea.getMeta()));
        mPublisher.onCompleted();
    }

    @Override
    public void uncrossoutIdea(Idea idea) {
        mIdeas.set(mIdeas.indexOf(idea),
                new Idea(idea.getId(), idea.getCategory(), idea.getContent(), false, idea.getType(), idea.getMeta()));
        mPublisher.onCompleted();
    }

    @Override
    public void removeIdea(Idea idea) {
        mIdeas.remove(idea);
        mPublisher.onCompleted();
    }

    @Override
    public void setSuggestions(List<Idea> ideas) {
        mIdeaSuggestions.clear();
        mIdeaSuggestions.addAll(ideas);
    }

    @Override
    public int getIdeaCount() {
        return mIdeas.size() + 1 + mIdeaSuggestions.size();
    }

    @Override
    public void subscribeToIdeaListChanges(Observer<List<Idea>> observer) {
        mPublisher.subscribe(observer);
    }

    @Override
    public Idea getIdeaAtPos(int pos) {
        if (pos < mIdeas.size()) {
            return mIdeas.get(pos);
        }
        pos -= mIdeas.size();
        if (pos < mBlankIdeas.size()) {
            return mBlankIdeas.get(pos);
        }
        pos -= mBlankIdeas.size();
        if (pos < mIdeaSuggestions.size()) {
            return mIdeaSuggestions.get(pos);
        }
        return null;
    }
}
