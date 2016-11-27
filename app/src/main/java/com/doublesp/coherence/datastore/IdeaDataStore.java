package com.doublesp.coherence.datastore;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    public void setSuggestions(List<Idea> ideas) {
        getSuggestions().clear();
        getSuggestions().addAll(ideas);
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
    public void clearSuggestions() {
        getSuggestions().clear();
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
        return getIdeas().get(pos);
    }

    @Override
    public Idea getSuggestionAtPos(int pos) {
        return getSuggestions().get(pos);
    }

    @Override
    public Plan getPlan() {
        List<Idea> ideas = getIdeas();
        // TODO: allow user to name the plan
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm:ss");
        StringBuilder titleBuilder =  new StringBuilder(mContext.getString(R.string.default_idea_prefix));
        titleBuilder.append(" ");
        titleBuilder.append(formatter.format(calendar.getTime()));
        return new Plan(ideas, titleBuilder.toString(), ConstantsAndUtils.getOwner(mContext));
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

    private List<Idea> getIdeas() {
        return mIdeaSnapshotStore.mIdeas;
    }

    private List<Idea> getSuggestions() {
        return mIdeaSnapshotStore.mSuggestions;
    }
}
