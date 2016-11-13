package com.doublesp.coherence.datastore;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.viewmodels.Idea;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by pinyaoting on 11/10/16.
 */

public class IdeaDataStore implements IdeaDataStoreInterface {

    List<Idea> mIdeas;
    List<Idea> mBlankIdeas;
    List<Idea> mIdeaSuggestions;
    PublishSubject<Integer> mPublisher;
    int mIdeaState;

    public IdeaDataStore() {
        mIdeas = new ArrayList<>();
        mBlankIdeas = new ArrayList<>();
        mBlankIdeas.add(Idea.newInstanceOfBlankIdea());
        mIdeaSuggestions = new ArrayList<>();
        mPublisher = PublishSubject.create();
        mIdeaState = R.id.idea_state_idle;
    }

    @Override
    public void setIdeaState(int state) {
        mIdeaState = state;
        mPublisher.onNext(state);
        mPublisher.onCompleted();
    }

    @Override
    public void addIdea(Idea idea) {
        mIdeas.add(idea);
    }

    @Override
    public void updateIdea(int pos, Idea idea) {
        if (pos == mIdeas.size()) {
            mIdeas.add(idea);
        } else {
            mIdeas.set(pos, idea);
        }
    }

    @Override
    public void removeIdea(int pos) {
        mIdeas.remove(pos);
    }

    @Override
    public void setSuggestions(List<Idea> ideas) {
        mIdeaSuggestions.clear();
        mIdeaSuggestions.addAll(ideas);
    }

    @Override
    public int getIdeaCount() {
        return mIdeas.size() + mBlankIdeas.size() + mIdeaSuggestions.size();
    }

    @Override
    public void subscribeToIdeaStateChanges(Observer<Integer> observer) {
        mPublisher.subscribeOn(Schedulers.immediate()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(observer);
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
