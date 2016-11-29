package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;

import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/26/16.
 */

public interface GoalInteractorInterface {

    int getGoalCount();

    Goal getGoalAtPos(int pos);

    void clearGoal();

    void bookmarkGoalAtPos(int pos);

    void search(String keyword);

    void searchGoalByIdeas(List<Idea> ideas);

    void subscribeToGoalStateChange(Observer<Integer> observer);

    int getSavedGoalCount();

    Goal getSavedGoalAtPos(int pos);

    void bookmarkSavedGoalAtPos(int pos);

    void loadBookmarkedGoals();

    void subscribeToSavedGoalStateChange(Observer<Integer> observer);

}
