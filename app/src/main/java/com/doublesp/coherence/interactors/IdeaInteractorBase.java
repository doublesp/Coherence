package com.doublesp.coherence.interactors;

import com.google.firebase.database.FirebaseDatabase;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaReducer;
import com.doublesp.coherence.viewmodels.Plan;

import java.util.List;

import rx.Observer;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

abstract public class IdeaInteractorBase implements IdeaInteractorInterface {

    DataStoreInterface mDataStore;

    public IdeaInteractorBase(DataStoreInterface ideaDataStore) {
        mDataStore = ideaDataStore;
    }

    abstract int getCategory();

    @Override
    public void acceptSuggestedIdeaAtPos(int pos) {
        mDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.ADD, mDataStore.getIdeaCount() - 1));
        Idea idea = mDataStore.getSuggestionAtPos(pos);
        mDataStore.addIdea(idea);
        mDataStore.setIdeaState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.ADD, mDataStore.getIdeaCount() - 1));
    }

    @Override
    public void crossoutIdea(int pos) {
        mDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.REMOVE));
        Idea idea = mDataStore.getIdeaAtPos(pos);
        IdeaReducer reducer = mDataStore.getIdeaReducer(idea.getId());
        if (reducer != null) {
            reducer.setCrossedOut(true);
        }
        mDataStore.moveIdeaToBottom(pos);
        mDataStore.setIdeaState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.REMOVE));
    }

    @Override
    public void uncrossoutIdea(int pos) {
        mDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.REMOVE));
        Idea idea = mDataStore.getIdeaAtPos(pos);
        IdeaReducer reducer = mDataStore.getIdeaReducer(idea.getId());
        if (reducer != null) {
            reducer.setCrossedOut(false);
        }
        mDataStore.moveIdeaToTop(pos);
        mDataStore.setIdeaState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.REMOVE));
    }

    @Override
    public void removeIdea(int pos) {
        mDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.REMOVE, pos, 1));
        mDataStore.removeIdea(pos);
        mDataStore.setIdeaState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.REMOVE, pos, 1));
    }

    @Override
    abstract public void getSuggestions(String keyword);

    @Override
    public void subscribeIdeaStateChange(Observer<ViewState> observer) {
        mDataStore.subscribeToIdeaStateChanges(observer);
    }

    @Override
    public void subscribeSuggestionStateChange(Observer<ViewState> observer) {
        mDataStore.subscribeToSuggestionStateChanges(observer);
    }

    @Override
    public int getIdeaCount() {
        return mDataStore.getIdeaCount();
    }

    @Override
    public int getSuggestionCount() {
        return mDataStore.getSuggestionCount();
    }

    @Override
    public Idea getIdeaAtPos(int pos) {
        return mDataStore.getIdeaAtPos(pos);
    }

    @Override
    public Idea getSuggestionAtPos(int pos) {
        return mDataStore.getSuggestionAtPos(pos);
    }

    @Override
    public Plan getPlan() {
        return mDataStore.getPlan();
    }

    @Override
    public void setPlan(Plan plan) {
        mDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing,
                ViewState.OPERATION.RELOAD));
        mDataStore.setPlan(plan);
        mDataStore.setIdeaState(new ViewState(
                R.id.state_loaded,
                ViewState.OPERATION.RELOAD));
    }

    @Override
    public Plan createPlan(String id, String name) {
        return mDataStore.createPlan(id, name);
    }


    @Override
    abstract public void loadPendingIdeas(Goal goal);

    @Override
    public void discardPlanIfEmpty() {
        Plan plan = mDataStore.getPlan();
        if (plan == null || plan.getId() == null) {
            return;
        }
        String listId = plan.getId();
        List<Idea> ideas = plan.getIdeas();
        if (ideas == null || ideas.isEmpty() || ideas.size() == 0) {
            // TODO: inject FirebaseDatabase
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(ConstantsAndUtils.USER_LISTS)
                    .child(ConstantsAndUtils.getOwner(getContext()))
                    .child(listId)
                    .removeValue();
        }
    }

    @Override
    public void clearPlan() {
        mDataStore.clearPlan();
    }

    @Override
    public int getPendingIdeasCount(String id) {
        return mDataStore.getPendingIdeasCount(id);
    }

    @Override
    public Idea getPendingIdea(String id, int pos) {
        return mDataStore.getPendingIdea(id, pos);
    }
}
