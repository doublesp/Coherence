package com.doublesp.coherence.interactors;

import android.os.Parcelable;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import org.parceler.Parcels;

import java.util.HashSet;
import java.util.Set;

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
        mIdeaDataStore.addIdea(
                new Idea("", getCategory(), content, false, R.id.idea_type_user_generated, null,
                        null));
        mIdeaDataStore.setIdeaState(R.id.idea_state_suggestion_loaded);
    }

    @Override
    public void acceptSuggestedIdeaAtPos(int pos) {
        mIdeaDataStore.setIdeaState(R.id.idea_state_refreshing);
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        mIdeaDataStore.removeIdea(pos);
        Set<String> dedupSet = new HashSet<>();
        for (Idea relatedIdea : idea.getRelatedIdeas()) {
            if (dedupSet.contains(relatedIdea.getContent())) {
                continue;
            }
            mIdeaDataStore.addIdea(relatedIdea);
            dedupSet.add(relatedIdea.getContent());
        }
        mIdeaDataStore.setIdeaState(R.id.idea_state_suggestion_loaded);
    }

    @Override
    public void updateIdea(int pos, String content) {
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        Idea newIdea = new Idea(idea.getId(), idea.getCategory(), content, idea.isCrossedOut(),
                R.id.idea_type_user_generated, idea.getMeta(), idea.getRelatedIdeas());
        mIdeaDataStore.updateIdea(pos, newIdea);
    }

    @Override
    public void crossoutIdea(int pos) {
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        Idea newIdea = new Idea(idea.getId(), idea.getCategory(), idea.getContent(), true,
                idea.getType(), idea.getMeta(), idea.getRelatedIdeas());
        mIdeaDataStore.updateIdea(pos, newIdea);
    }

    @Override
    public void uncrossoutIdea(int pos) {
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        Idea newIdea = new Idea(idea.getId(), idea.getCategory(), idea.getContent(), false,
                idea.getType(), idea.getMeta(), idea.getRelatedIdeas());
        mIdeaDataStore.updateIdea(pos, newIdea);
    }

    @Override
    public void removeIdea(int pos) {
        mIdeaDataStore.removeIdea(pos);
    }

    @Override
    abstract public void getSuggestions(String keyword);

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

    @Override
    public Parcelable getSnapshot() {
        return mIdeaDataStore.getSnapshot();
    }

    @Override
    public Plan getPlan() {
        return mIdeaDataStore.getPlan();
    }

    @Override
    public Parcelable getParcelablePlan() {
        return Parcels.wrap(mIdeaDataStore.getPlan());
    }

    @Override
    abstract public String getSharableContent();
}
