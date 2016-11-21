package com.doublesp.coherence.interfaces.domain;

import android.os.Parcelable;

import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface IdeaInteractorInterface {

    void addIdea(String content);

    void acceptSuggestedIdeaAtPos(int pos);

    void updateIdea(int pos, String content);

    void crossoutIdea(int pos);

    void uncrossoutIdea(int pos);

    void removeIdea(int pos);

    void getSuggestions(String keyword);

    void subscribe(Observer<Integer> observer);

    int getIdeaCount();

    int getUserIdeaCount();

    Idea getIdeaAtPos(int pos);

    String getSharableContent();

    Parcelable getSnapshot();

    Parcelable getParcelablePlan();

    Plan getPlan();

}
