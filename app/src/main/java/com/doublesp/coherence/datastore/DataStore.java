package com.doublesp.coherence.datastore;

import android.content.Context;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

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

public class DataStore implements DataStoreInterface {

    DataSnapshotStore mSnapshotStore;
    List<Observer<Integer>> mIdeaStateObservers;
    List<Observer<Integer>> mSuggestionStateObservers;
    List<Observer<Integer>> mSavedGoalStateObservers;
    List<Observer<Integer>> mGoalStateObservers;
    Plan mPlan;
    int mIdeaState;
    int mSuggestionState;
    int mSavedGoalState;
    int mGoalState;
    private Context mContext;

    public DataStore(Context context) {
        mSnapshotStore = new DataSnapshotStore();
        mIdeaStateObservers = new ArrayList<>();
        mSuggestionStateObservers = new ArrayList<>();
        mSavedGoalStateObservers = new ArrayList<>();
        mGoalStateObservers = new ArrayList<>();
        mIdeaState = R.id.state_idle;
        mSuggestionState = R.id.state_idle;
        mSavedGoalState = R.id.state_idle;
        mGoalState = R.id.state_idle;
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
    public void setGoalState(int state) {
        mGoalState = state;
        notifyGoalStateChange();
    }

    @Override
    public void setSavedGoalState(int state) {
        mSavedGoalState = state;
        notifySavedGoalStateChange();
    }

    @Override
    public void addIdea(Idea idea) {
        getIdeas().add(idea);
    }

    @Override
    public void setIdeas(List<Idea> ideas) {
        getIdeas().clear();
        getIdeas().addAll(ideas);
    }

    @Override
    public void updateIdea(int pos, Idea idea) {
        if (pos == getIdeas().size()) {
            getIdeas().add(idea);
        } else {
            getIdeas().set(pos, idea);
        }
    }

    @Override
    public void removeIdea(int pos) {
        getIdeas().remove(pos);
    }

    @Override
    public void clearIdeas() {
        getIdeas().clear();
    }

    @Override
    public void setSuggestions(List<Idea> ideas) {
        getSuggestions().clear();
        getSuggestions().addAll(ideas);
    }

    @Override
    public void setGoals(List<Goal> goals) {
        getGoals().clear();
        getGoals().addAll(goals);
    }

    @Override
    public void setSavedGoals(List<Goal> goals) {
        getSavedGoals().clear();
        getSavedGoals().addAll(goals);
    }

    @Override
    public void updateGoal(int pos, Goal goal) {
        getGoals().set(pos, goal);
    }

    @Override
    public void updateSavedGoal(int pos, Goal goal) {
        getSavedGoals().set(pos, goal);
    }

    @Override
    public int getIdeaCount() {
        return getIdeas().size();
    }

    @Override
    public int getSuggestionCount() {
        return getSuggestions().size();
    }

    @Override
    public int getGoalCount() {
        return getGoals().size();
    }

    @Override
    public int getSavedGoalCount() {
        return getSavedGoals().size();
    }

    @Override
    public void clearSuggestions() {
        getSuggestions().clear();
    }

    @Override
    public void clearGoals() {
        getGoals().clear();
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
    public void subscribeToGoalStateChanges(Observer<Integer> observer) {
        mGoalStateObservers.add(observer);
    }

    @Override
    public void subscribeToSavedGoalStateChanges(Observer<Integer> observer) {
        mSavedGoalStateObservers.add(observer);
    }

    @Override
    public Idea getIdeaAtPos(int pos) {
        return getIdeas().get(pos);
    }

    @Override
    public Idea getSuggestionAtPos(int pos) {
        return getSuggestions().get(pos);
    }

    @Override
    public Goal getGoalAtPos(int pos) {
        return getGoals().get(pos);
    }

    @Override
    public Goal getSavedGoalAtPos(int pos) {
        return getSavedGoals().get(pos);
    }

    @Override
    public Plan getPlan() {
        return mPlan;
    }

    @Override
    public Plan createPlan(String id) {
        mPlan = new Plan(id, getIdeas(), ConstantsAndUtils.getDefaultTitle(mContext),
                ConstantsAndUtils.getOwner(mContext));
        return mPlan;
    }

    @Override
    public void setPlan(Plan plan) {
        mPlan = plan;
        mSnapshotStore.setIdeas(mPlan.getIdeas());
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
        ConnectableObservable<Integer> connectedObservable = Observable.just(
                mSuggestionState).publish();
        for (Observer<Integer> observer : mSuggestionStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }

    private void notifySavedGoalStateChange() {
        ConnectableObservable<Integer> connectedObservable = Observable.just(
                mSavedGoalState).publish();
        for (Observer<Integer> observer : mSavedGoalStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }

    private void notifyGoalStateChange() {
        ConnectableObservable<Integer> connectedObservable = Observable.just(mGoalState).publish();
        for (Observer<Integer> observer : mGoalStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }

    private List<Idea> getIdeas() {
        return mSnapshotStore.getIdeas();
    }

    private List<Idea> getSuggestions() {
        return mSnapshotStore.getSuggestions();
    }

    private List<Goal> getGoals() {
        return mSnapshotStore.getGoals();
    }

    private List<Goal> getSavedGoals() {
        return mSnapshotStore.getSavedGoals();
    }
}
