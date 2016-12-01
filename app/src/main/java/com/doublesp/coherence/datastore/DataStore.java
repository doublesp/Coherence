package com.doublesp.coherence.datastore;

import android.content.Context;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
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
    List<Observer<ViewState>> mIdeaStateObservers;
    List<Observer<ViewState>> mSuggestionStateObservers;
    List<Observer<ViewState>> mSavedGoalStateObservers;
    List<Observer<ViewState>> mGoalStateObservers;
    Plan mPlan;
    ViewState mIdeaState;
    ViewState mSuggestionState;
    ViewState mSavedGoalState;
    ViewState mGoalState;
    private Context mContext;

    public DataStore(Context context) {
        mSnapshotStore = new DataSnapshotStore();
        mIdeaStateObservers = new ArrayList<>();
        mSuggestionStateObservers = new ArrayList<>();
        mSavedGoalStateObservers = new ArrayList<>();
        mGoalStateObservers = new ArrayList<>();
        mIdeaState = new ViewState(R.id.state_idle);
        mSuggestionState = new ViewState(R.id.state_idle);
        mSavedGoalState = new ViewState(R.id.state_idle);
        mGoalState = new ViewState(R.id.state_idle);
        mContext = context;
    }

    @Override
    public void setIdeaState(ViewState state) {
        mIdeaState = state;
        notifyIdeaStateChange();
    }

    @Override
    public void setSuggestionState(ViewState state) {
        mSuggestionState = state;
        notifySuggestionStateChange();
    }

    @Override
    public void setGoalState(ViewState state) {
        mGoalState = state;
        notifyGoalStateChange();
    }

    @Override
    public void setSavedGoalState(ViewState state) {
        mSavedGoalState = state;
        notifySavedGoalStateChange();
    }

    @Override
    public void addIdea(Idea idea) {
        getIdeas().add(idea);
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
    public void subscribeToIdeaStateChanges(Observer<ViewState> observer) {
        mIdeaStateObservers.add(observer);
    }

    @Override
    public void subscribeToSuggestionStateChanges(Observer<ViewState> observer) {
        mSuggestionStateObservers.add(observer);
    }

    @Override
    public void subscribeToGoalStateChanges(Observer<ViewState> observer) {
        mGoalStateObservers.add(observer);
    }

    @Override
    public void subscribeToSavedGoalStateChanges(Observer<ViewState> observer) {
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
    public void setPlan(Plan plan) {
        mPlan = plan;
        mSnapshotStore.setIdeas(mPlan.getIdeas());
    }

    @Override
    public Plan createPlan(String id) {
        mPlan = new Plan(id, getIdeas(), ConstantsAndUtils.getDefaultTitle(mContext),
                ConstantsAndUtils.getOwner(mContext));
        return mPlan;
    }

    private void notifyIdeaStateChange() {
        ConnectableObservable<ViewState> connectedObservable = Observable.just(mIdeaState).publish();
        for (Observer<ViewState> observer : mIdeaStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }

    private void notifySuggestionStateChange() {
        ConnectableObservable<ViewState> connectedObservable = Observable.just(
                mSuggestionState).publish();
        for (Observer<ViewState> observer : mSuggestionStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }

    private void notifySavedGoalStateChange() {
        ConnectableObservable<ViewState> connectedObservable = Observable.just(
                mSavedGoalState).publish();
        for (Observer<ViewState> observer : mSavedGoalStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }

    private void notifyGoalStateChange() {
        ConnectableObservable<ViewState> connectedObservable = Observable.just(mGoalState).publish();
        for (Observer<ViewState> observer : mGoalStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }

    private List<Idea> getIdeas() {
        return mSnapshotStore.getIdeas();
    }

    @Override
    public void setIdeas(List<Idea> ideas) {
        getIdeas().clear();
        getIdeas().addAll(ideas);
    }

    private List<Idea> getSuggestions() {
        return mSnapshotStore.getSuggestions();
    }

    @Override
    public void setSuggestions(List<Idea> ideas) {
        getSuggestions().clear();
        getSuggestions().addAll(ideas);
    }

    private List<Goal> getGoals() {
        return mSnapshotStore.getGoals();
    }

    @Override
    public void setGoals(List<Goal> goals) {
        getGoals().clear();
        getGoals().addAll(goals);
    }

    private List<Goal> getSavedGoals() {
        return mSnapshotStore.getSavedGoals();
    }

    @Override
    public void setSavedGoals(List<Goal> goals) {
        getSavedGoals().clear();
        getSavedGoals().addAll(goals);
    }
}
