package com.doublesp.coherence.datastore;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.viewmodels.Idea;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by pinyaoting on 11/10/16.
 */

public class IdeaDataStore implements IdeaDataStoreInterface {

    List<Idea> mIdeas;
    List<Idea> mBlankIdeas;
    List<Idea> mIdeaSuggestions;
    List<Observer<Integer>> mStateObservers;
    int mIdeaState;

    public IdeaDataStore() {
        mIdeas = new ArrayList<>();
        mBlankIdeas = new ArrayList<>();
        mBlankIdeas.add(Idea.newInstanceOfBlankIdea());
        mIdeaSuggestions = new ArrayList<>();
        mStateObservers = new ArrayList<>();
        mIdeaState = R.id.idea_state_idle;
    }

    @Override
    public void setIdeaState(int state) {
        mIdeaState = state;
        notifyStateChange();
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
        Pair<Integer, List<Idea>> adjustedPair = getAdjustedPositionAndCorrespondingList(pos);
        int adjustedPos = adjustedPair.first;
        List<Idea> targetList = adjustedPair.second;
        targetList.remove(adjustedPos);
    }

    @Override
    public void setSuggestions(List<Idea> ideas) {
        mIdeaSuggestions.clear();
        mIdeaSuggestions.addAll(ideas);
    }

    @Override
    public List<Idea> getSuggestions() {
        return mIdeaSuggestions;
    }

    @Override
    public int getIdeaCount() {
        return mIdeas.size() + mBlankIdeas.size() + mIdeaSuggestions.size();
    }

    @Override
    public int getUserIdeaCount() {
        return mIdeas.size();
    }

    @Override
    public void subscribeToIdeaStateChanges(Observer<Integer> observer) {
        mStateObservers.add(observer);
    }

    @Override
    public Idea getIdeaAtPos(int pos) {
        Pair<Integer, List<Idea>> adjustedPair = getAdjustedPositionAndCorrespondingList(pos);
        int adjustedPos = adjustedPair.first;
        List<Idea> targetList = adjustedPair.second;
        return targetList.get(adjustedPos);
    }

    private Pair<Integer, List<Idea>> getAdjustedPositionAndCorrespondingList(int pos) {
        if (pos < mIdeas.size()) {
            return new Pair<>(pos, mIdeas);
        }
        pos -= mIdeas.size();
        if (pos < mBlankIdeas.size()) {
            return new Pair<>(pos, mBlankIdeas);
        }
        pos -= mBlankIdeas.size();
        if (pos < mIdeaSuggestions.size()) {
            return new Pair<>(pos, mIdeaSuggestions);
        }
        return null;
    }

    private void notifyStateChange() {
        ConnectableObservable<Integer> connectedObservable = Observable.just(mIdeaState).publish();
        for (Observer<Integer> observer : mStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }
}
