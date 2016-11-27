package com.doublesp.coherence.datastore;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import android.content.Context;
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

    IdeaSnapshotStore mIdeaSnapshotStore;
    List<Observer<Integer>> mIdeaStateObservers;
    List<Observer<Integer>> mSuggestionStateObservers;
    int mIdeaState;
    int mSuggestionState;
    private Context mContext;

    public IdeaDataStore(Context context) {
        mIdeaSnapshotStore = new IdeaSnapshotStore();
        mIdeaStateObservers = new ArrayList<>();
        mSuggestionStateObservers = new ArrayList<>();
        mIdeaState = R.id.idea_state_idle;
        mSuggestionState = R.id.suggestion_state_idle;
        mContext = context;
    }

    @Override
    public void setIdeaState(int state) {
        mIdeaState = state;
        notifyIdeaStateChange();
    }

    @Override
    public void setSuggestionState(int state) {
        mSuggestionState = state;
        notifySuggestionStateChange();
    }


    @Override
    public void addIdea(Idea idea) {
        getUserIdeas().add(idea);
    }

    @Override
    public void updateIdea(int pos, Idea idea) {
        if (pos == getUserIdeas().size()) {
            getUserIdeas().add(idea);
        } else {
            getUserIdeas().set(pos, idea);
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
        getSuggestedIdeas().clear();
        getSuggestedIdeas().addAll(ideas);
    }

    @Override
    public int getIdeaCount() {
        return getUserIdeas().size() + getSuggestedIdeas().size();
    }

    @Override
    public int getSuggestionCount() {
        return getSuggestedIdeas().size();
    }


    @Override
    public void clearSuggestions() {
        getSuggestedIdeas().clear();
    }

    @Override
    public void subscribeToIdeaStateChanges(Observer<Integer> observer) {
        mIdeaStateObservers.add(observer);
    }

    @Override
    public void subscribeToSuggestionStateChanges(Observer<Integer> observer) {
        mSuggestionStateObservers.add(observer);
    }

    @Override
    public Idea getIdeaAtPos(int pos) {
        Pair<Integer, List<Idea>> adjustedPair = getAdjustedPositionAndCorrespondingList(pos);
        int adjustedPos = adjustedPair.first;
        List<Idea> targetList = adjustedPair.second;
        return targetList.get(adjustedPos);
    }

    @Override
    public Idea getSuggestionAtPos(int pos) {
        return null;
    }

    @Override
    public Plan getPlan() {
        List<Idea> ideas = getUserIdeas();
        // TODO: allow user to name the plan
        return new Plan(ideas, "", ConstantsAndUtils.getOwner(mContext));
    }

    private Pair<Integer, List<Idea>> getAdjustedPositionAndCorrespondingList(int pos) {
        if (pos < getUserIdeas().size()) {
            return new Pair<>(pos, getUserIdeas());
        }
        pos -= getUserIdeas().size();
        if (pos < getSuggestedIdeas().size()) {
            return new Pair<>(pos, getSuggestedIdeas());
        }
        return null;
    }

    private void notifyIdeaStateChange() {
        ConnectableObservable<Integer> connectedObservable = Observable.just(mIdeaState).publish();
        for (Observer<Integer> observer : mIdeaStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }

    private void notifySuggestionStateChange() {
        ConnectableObservable<Integer> connectedObservable = Observable.just(mSuggestionState).publish();
        for (Observer<Integer> observer : mSuggestionStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }

    private List<Idea> getUserIdeas() {
        return mIdeaSnapshotStore.mIdeas;
    }

    private List<Idea> getSuggestedIdeas() {
        return mIdeaSnapshotStore.mIdeaSuggestions;
    }
}
