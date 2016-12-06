package com.doublesp.coherence.viewmodels;

public class GoalReducer {

    Goal mGoal;

    public GoalReducer(Goal goal) {
        mGoal = goal;
    }

    public void setId(String id) {
        mGoal.id = id;
    }

    public void setTitle(String title) {
        mGoal.title = title;
    }

    public void setDescription(String description) {
        mGoal.description = description;
    }

    public void setImageUrl(String imageUrl) {
        mGoal.imageUrl = imageUrl;
    }

    public void setBookmarked(boolean bookmarked) {
        mGoal.bookmarked = bookmarked;
    }
}
