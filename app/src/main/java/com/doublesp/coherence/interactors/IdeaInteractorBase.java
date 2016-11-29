package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import java.util.List;

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
    public void addIdea(String content) {
        mIdeaDataStore.setIdeaState(R.id.state_refreshing);
        mIdeaDataStore.addIdea(
                new Idea("", getCategory(), content, false, R.id.idea_type_user_generated, null));
        mIdeaDataStore.setIdeaState(R.id.state_loaded);
    }

    @Override
    public void setIdeas(List<Idea> ideas) {
        mIdeaDataStore.setIdeaState(R.id.state_refreshing);
        mIdeaDataStore.setIdeas(ideas);
        mIdeaDataStore.setIdeaState(R.id.state_loaded);
    }

    @Override
    public void acceptSuggestedIdeaAtPos(int pos) {
        mIdeaDataStore.setIdeaState(R.id.state_refreshing);
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        mIdeaDataStore.addIdea(idea);
        mIdeaDataStore.setIdeaState(R.id.state_loaded);

        mIdeaDataStore.setSuggestionState(R.id.state_refreshing);
        mIdeaDataStore.clearSuggestions();
        mIdeaDataStore.setSuggestionState(R.id.state_loaded);
    }

    @Override
    public void updateIdea(int pos, String content) {
        mIdeaDataStore.setIdeaState(R.id.state_refreshing);
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        Idea newIdea = new Idea(idea.getId(), idea.getCategory(), content, idea.isCrossedOut(),
                R.id.idea_type_user_generated, idea.getMeta());
        mIdeaDataStore.updateIdea(pos, newIdea);
        mIdeaDataStore.setIdeaState(R.id.state_loaded);
    }

    @Override
    public void crossoutIdea(int pos) {
        mIdeaDataStore.setIdeaState(R.id.state_refreshing);
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        Idea newIdea = new Idea(idea.getId(), idea.getCategory(), idea.getContent(), true,
                idea.getType(), idea.getMeta());
        mIdeaDataStore.updateIdea(pos, newIdea);
        mIdeaDataStore.setIdeaState(R.id.state_loaded);
    }

    @Override
    public void uncrossoutIdea(int pos) {
        mIdeaDataStore.setIdeaState(R.id.state_refreshing);
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        Idea newIdea = new Idea(idea.getId(), idea.getCategory(), idea.getContent(), false,
                idea.getType(), idea.getMeta());
        mIdeaDataStore.updateIdea(pos, newIdea);
        mIdeaDataStore.setIdeaState(R.id.state_loaded);
    }

    @Override
    public void removeIdea(int pos) {
        mIdeaDataStore.setIdeaState(R.id.state_refreshing);
        mIdeaDataStore.removeIdea(pos);
        mIdeaDataStore.setIdeaState(R.id.state_loaded);
    }

    @Override
    public void clearIdeas() {
        mIdeaDataStore.setIdeaState(R.id.state_refreshing);
        mIdeaDataStore.clearIdeas();
        mIdeaDataStore.setIdeaState(R.id.state_loaded);
    }

    @Override
    abstract public void getSuggestions(String keyword);

    @Override
    public void subscribeIdeaStateChange(Observer<Integer> observer) {
        mIdeaDataStore.subscribeToIdeaStateChanges(observer);
    }

    @Override
    public void subscribeSuggestionStateChange(Observer<Integer> observer) {
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

    @Deprecated // use create plan
    @Override
    public Plan getPlan() {
        return mIdeaDataStore.getPlan();
    }

    @Override
    public Plan createPlan(String id) {
        return mIdeaDataStore.createPlan(id);
    }

    @Override
    abstract public void loadIdeasFromGoal(Goal goal);
}
