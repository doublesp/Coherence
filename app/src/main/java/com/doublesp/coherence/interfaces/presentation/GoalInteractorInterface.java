package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.viewmodels.Goal;

import rx.Observer;

/**
 * Created by pinyaoting on 11/26/16.
 */

public interface GoalInteractorInterface {

    int getGoalCount();

    Goal getGoalAtPos(int pos);

    void bookmarkGoalAtPos(int pos);

    void search(String keyword);

    void subscribeToGoalStateChange(Observer<Integer> observer);

    int getSavedGoalCount();

    Goal getSavedGoalAtPos(int pos);

    void bookmarkSavedGoalAtPos(int pos);

    void loadBookmarkedGoals();

    void subscribeToSavedGoalStateChange(Observer<Integer> observer);

}
