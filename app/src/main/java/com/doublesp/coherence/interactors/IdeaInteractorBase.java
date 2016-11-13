package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.viewmodels.Idea;

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
        mIdeaDataStore.setIdeaState(R.id.idea_state_creation_pending);
        mIdeaDataStore.addIdea(new Idea(0L, getCategory(), content, false, R.id.idea_type_user_generated, null));
        mIdeaDataStore.setIdeaState(R.id.idea_state_creation_settled);
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
    public Idea getIdeaAtPos(int pos) {
        return mIdeaDataStore.getIdeaAtPos(pos);
    }
}
