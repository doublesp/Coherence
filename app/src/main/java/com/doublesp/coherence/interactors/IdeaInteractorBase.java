package com.doublesp.coherence.interactors;

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

    @Override
    public void createIdea(Idea idea) {
        mIdeaDataStore.addIdea(idea);
    }

    @Override
    public void crossoutIdea(Idea idea) {
        mIdeaDataStore.crossoutIdea(idea);
    }

    @Override
    public void uncrossoutIdea(Idea idea) {
        mIdeaDataStore.uncrossoutIdea(idea);
    }

    @Override
    public void removeIdea(Idea idea) {
        mIdeaDataStore.removeIdea(idea);
    }

    @Override
    abstract public void getRelatedIdeas(Idea idea);

    @Override
    public void subscribe(Observer observer) {
        mIdeaDataStore.subscribeToIdeaListChanges(observer);
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
