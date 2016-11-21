package com.doublesp.coherence.interfaces.domain;

import android.os.Parcelable;

import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface IdeaDataStoreInterface {

    void setIdeaState(int state);

    void addIdea(Idea idea);

    void setCurrentIdea(Idea idea);

    void updateIdea(int pos, Idea idea);

    void removeIdea(int pos);

    List<Idea> getIdeas();

    List<Idea> getSuggestions();

    void setSuggestions(List<Idea> ideas);

    Idea getIdeaAtPos(int pos);

    int getIdeaCount();

    int getUserIdeaCount();

    void subscribeToIdeaStateChanges(Observer<Integer> observer);

    Parcelable getSnapshot();

    void setSnapshot(Parcelable ideaSnapshot);

    Plan getPlan();
}
