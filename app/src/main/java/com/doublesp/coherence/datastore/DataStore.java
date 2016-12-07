package com.doublesp.coherence.datastore;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.GoalReducer;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaReducer;
import com.doublesp.coherence.viewmodels.Plan;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

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
    private Map<String, IdeaReducer> mIdeaReducers;
    private Map<String, GoalReducer> mExploreGoalReducers;
    private Map<String, GoalReducer> mSavedGoalReducers;
    private Context mContext;
    private int mDisplayGoalFlag;

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
        mDisplayGoalFlag = R.id.flag_explore_recipes;
        mIdeaReducers = new HashMap<>();
        mExploreGoalReducers = new HashMap<>();
        mSavedGoalReducers = new HashMap<>();
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
    public void addIdea(Idea idea) {
        mIdeaReducers.put(idea.getId(), new IdeaReducer(idea));
        getIdeas().add(idea);
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
    public void clearGoals() {
        getExploreGoals().clear();
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
    public Idea getIdeaAtPos(int pos) {
        return getIdeas().get(pos);
    }

    @Override
    public Idea getSuggestionAtPos(int pos) {
        return getSuggestions().get(pos);
    }

    @Override
    public Plan getPlan() {
        return mPlan;
    }

    @Override
    public void setPlan(Plan plan) {
        mPlan = plan;
        mIdeaReducers.clear();
        List<Idea> ideas = plan.getIdeas();
        if (ideas != null) {
            for (Idea idea : ideas) {
                mIdeaReducers.put(idea.getId(), new IdeaReducer(idea));
            }
        }
        mSnapshotStore.setIdeas(mPlan.getIdeas());
    }

    @Override
    public Plan createPlan(String id) {
        mPlan = new Plan(id, getIdeas(), ConstantsAndUtils.getDefaultTitle(mContext),
                ConstantsAndUtils.getOwner(mContext));
        return mPlan;
    }

    private void notifyIdeaStateChange() {
        ConnectableObservable<ViewState> connectedObservable = Observable.just(
                mIdeaState).publish();
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

    private void notifyGoalStateChange() {
        ConnectableObservable<ViewState> connectedObservable = Observable.just(
                mGoalState).publish();
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
        mIdeaReducers.clear();
        for (Idea idea : ideas) {
            mIdeaReducers.put(idea.getId(), new IdeaReducer(idea));
        }
        getIdeas().addAll(ideas);
    }

    @Override
    public void moveIdeaToBottom(int pos) {
        if (getIdeas().size() <= pos) {
            // out of bound
            return;
        }
        getIdeas().add(getIdeas().remove(pos));
    }

    @Override
    public void moveIdeaToTop(int pos) {
        if (getIdeas().size() <= pos) {
            // out of bound
            return;
        }
        getIdeas().add(0, getIdeas().remove(pos));
    }

    private List<Idea> getSuggestions() {
        return mSnapshotStore.getSuggestions();
    }

    @Override
    public void setSuggestions(List<Idea> ideas) {
        getSuggestions().clear();
        getSuggestions().addAll(ideas);
    }

    private List<Goal> getExploreGoals() {
        return mSnapshotStore.getExploreGoals();
    }

    @Override
    public void setExploreGoals(List<Goal> goals) {
        getExploreGoals().clear();
        mExploreGoalReducers.clear();
        for (Goal goal : goals) {
            mExploreGoalReducers.put(goal.getId(), new GoalReducer(goal));
        }
        getExploreGoals().addAll(goals);
    }

    private List<Goal> getSavedGoals() {
        return mSnapshotStore.getSavedGoals();
    }

    @Override
    public void setSavedGoals(List<Goal> goals) {
        getSavedGoals().clear();
        mSavedGoalReducers.clear();
        for (Goal goal : goals) {
            mSavedGoalReducers.put(goal.getId(), new GoalReducer(goal));
        }
        getSavedGoals().addAll(goals);
    }

    @Override
    public IdeaReducer getIdeaReducer(String id) {
        return mIdeaReducers.get(id);
    }

    @Override
    public GoalReducer getExploreGoalReducer(String id) {
        return mExploreGoalReducers.get(id);
    }

    @Override
    public GoalReducer getSavedGoalReducer(String id) {
        return mSavedGoalReducers.get(id);
    }

    @Override
    public Goal getGoalAtPos(int pos) {
        switch (mDisplayGoalFlag) {
            case R.id.flag_explore_recipes:
                return getExploreGoals().get(pos);
            case R.id.flag_saved_recipes:
                return getSavedGoals().get(pos);
        }
        return null;
    }

    @Override
    public int getGoalFlag() {
        return mDisplayGoalFlag;
    }

    @Override
    public void setGoalFlag(int flag) {
        mDisplayGoalFlag = flag;
    }

    @Override
    public int getGoalCount() {
        switch (mDisplayGoalFlag) {
            case R.id.flag_explore_recipes:
                return getExploreGoals().size();
            case R.id.flag_saved_recipes:
                return getSavedGoals().size();
        }
        return 0;
    }

    @Override
    public void clearPlan() {
        mPlan = null;
        mIdeaReducers.clear();
        mSnapshotStore.getIdeas().clear();
    }

    private Map<String, List<Idea>> getPendingIdeas() {
        return mSnapshotStore.getPendingIdeas();
    }

    @Override
    public void setPendingIdeas(String id, List<Idea> pendingIdeas) {
        getPendingIdeas().put(id, pendingIdeas);
    }

    @Override
    public void loadPendingIdeas(String id) {
        if (getPendingIdeas().containsKey(id)) {
            setIdeas(getPendingIdeas().get(id));
        }
    }

    @Override
    public int getPendingIdeasCount(String id) {
        if (getPendingIdeas().containsKey(id)) {
            List<Idea> ideas = getPendingIdeas().get(id);
            if (ideas != null) {
                return ideas.size();
            }
        }
        return 0;
    }

    @Override
    public Idea getPendingIdea(String id, int pos) {
        if (getPendingIdeas().containsKey(id)) {
            List<Idea> ideas = getPendingIdeas().get(id);
            if (ideas.size() > pos) {
                return ideas.get(pos);
            }
        }
        return null;
    }
}
