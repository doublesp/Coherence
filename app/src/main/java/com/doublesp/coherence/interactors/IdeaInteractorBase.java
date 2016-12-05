package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaReducer;
import com.doublesp.coherence.viewmodels.Plan;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

abstract public class IdeaInteractorBase implements IdeaInteractorInterface {

    DataStoreInterface mIdeaDataStore;

    public IdeaInteractorBase(DataStoreInterface ideaDataStore) {
        mIdeaDataStore = ideaDataStore;
    }

    abstract int getCategory();

    @Override
    public void acceptSuggestedIdeaAtPos(int pos) {
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.ADD, mIdeaDataStore.getIdeaCount() - 1));
        Idea idea = mIdeaDataStore.getSuggestionAtPos(pos);
        mIdeaDataStore.addIdea(idea);
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.ADD, mIdeaDataStore.getIdeaCount() - 1));
    }

    @Override
    public void crossoutIdea(int pos) {
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.UPDATE, pos, 1));
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        IdeaReducer reducer = mIdeaDataStore.getIdeaReducer(idea.getId());
        reducer.setCrossedOut(true);
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.UPDATE, pos, 1));
    }

    @Override
    public void uncrossoutIdea(int pos) {
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.UPDATE, pos, 1));
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        IdeaReducer reducer = mIdeaDataStore.getIdeaReducer(idea.getId());
        reducer.setCrossedOut(false);
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.UPDATE, pos, 1));
    }

    @Override
    public void removeIdea(int pos) {
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.REMOVE, pos, 1));
        mIdeaDataStore.removeIdea(pos);
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.REMOVE, pos, 1));
    }

    @Override
    public void clearIdeas() {
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.CLEAR));
        mIdeaDataStore.clearIdeas();
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.CLEAR));
    }

    @Override
    abstract public void getSuggestions(String keyword);

    @Override
    public void subscribeIdeaStateChange(Observer<ViewState> observer) {
        mIdeaDataStore.subscribeToIdeaStateChanges(observer);
    }

    @Override
    public void subscribeSuggestionStateChange(Observer<ViewState> observer) {
        mIdeaDataStore.subscribeToSuggestionStateChanges(observer);
    }

    @Override
    public int getIdeaCount() {
        return mIdeaDataStore.getIdeaCount();
    }

    @Override
    public int getSuggestionCount() {
        return mIdeaDataStore.getSuggestionCount();
    }

    @Override
    public Idea getIdeaAtPos(int pos) {
        return mIdeaDataStore.getIdeaAtPos(pos);
    }

    @Override
    public Idea getSuggestionAtPos(int pos) {
        return mIdeaDataStore.getSuggestionAtPos(pos);
    }

    @Override
    public Plan getPlan() {
        return mIdeaDataStore.getPlan();
    }

    @Override
    public void setPlan(Plan plan) {
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing,
                ViewState.OPERATION.RELOAD));
        mIdeaDataStore.setPlan(plan);
        mIdeaDataStore.setIdeaState(new ViewState(
                R.id.state_loaded,
                ViewState.OPERATION.RELOAD));
    }

    @Override
    public Plan createPlan(String id) {
        return mIdeaDataStore.createPlan(id);
    }

    @Override
    abstract public void loadIdeasFromGoal(Goal goal);
}
