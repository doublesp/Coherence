package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.viewmodels.Idea;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

abstract public class IdeaInteractorBase implements IdeaInteractorInterface {

    IdeaDataStoreInterface mIdeaDataStore;

    public IdeaInteractorBase(IdeaDataStoreInterface ideaDataStore) {
        mIdeaDataStore = ideaDataStore;
    }

    abstract int getCategory();

    @Override
    public void addIdea(String content) {
        mIdeaDataStore.setIdeaState(R.id.idea_state_refreshing);
        mIdeaDataStore.addIdea(new Idea(0L, getCategory(), content, false, R.id.idea_type_user_generated, null));
        // TODO: The reason of doing randomize on suggestions is to give you the feeling that suggestions are updated
        // each time you created an idea. We need to fetch suggestions from backend instead.
        randomize(mIdeaDataStore.getSuggestions());
        mIdeaDataStore.setIdeaState(R.id.idea_state_loaded);
    }

    @Override
    public void acceptSuggestedIdeaAtPos(int pos) {
        mIdeaDataStore.setIdeaState(R.id.idea_state_refreshing);
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        mIdeaDataStore.removeIdea(pos);
        mIdeaDataStore.addIdea(new Idea(idea.getId(), idea.getCategory(), idea.getContent(), idea.isCrossedOut(), R.id.idea_type_user_generated, idea.getMeta()));
        // TODO: The reason of doing randomize on suggestions is to give you the feeling that suggestions are updated
        // each time you accepat a suggestion. We need to fetch suggestions from backend instead.
        randomize(mIdeaDataStore.getSuggestions());
        mIdeaDataStore.setIdeaState(R.id.idea_state_loaded);
    }

    @Override
    public void updateIdea(int pos, String content) {
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        Idea newIdea = new Idea(idea.getId(), idea.getCategory(), content, idea.isCrossedOut(), R.id.idea_type_user_generated, idea.getMeta());
        mIdeaDataStore.updateIdea(pos, newIdea);
    }

    @Override
    public void crossoutIdea(int pos) {
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        Idea newIdea = new Idea(idea.getId(), idea.getCategory(), idea.getContent(), true, idea.getType(), idea.getMeta());
        mIdeaDataStore.updateIdea(pos, newIdea);
    }

    @Override
    public void uncrossoutIdea(int pos) {
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        Idea newIdea = new Idea(idea.getId(), idea.getCategory(), idea.getContent(), false, idea.getType(), idea.getMeta());
        mIdeaDataStore.updateIdea(pos, newIdea);
    }

    @Override
    public void removeIdea(int pos) {
        mIdeaDataStore.removeIdea(pos);
    }

    @Override
    abstract public void getRelatedIdeas(Idea idea);

    @Override
    public void subscribe(Observer<Integer> observer) {
        mIdeaDataStore.subscribeToIdeaStateChanges(observer);
    }

    @Override
    public int getIdeaCount() {
        return mIdeaDataStore.getIdeaCount();
    }

    @Override
    public int getUserIdeaCount() {
        return mIdeaDataStore.getUserIdeaCount();
    }

    @Override
    public Idea getIdeaAtPos(int pos) {
        return mIdeaDataStore.getIdeaAtPos(pos);
    }

    private void randomize(List list) {
        long seed = System.nanoTime();
        Collections.shuffle(list, new Random(seed));
    }
}
