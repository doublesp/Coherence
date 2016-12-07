package com.doublesp.coherence.datastore;

import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class DataSnapshotStore {

    private List<Idea> mIdeas;
    private List<Idea> mPendingIdeas;
    private List<Idea> mSuggestions;
    private List<Goal> mSavedGoals;
    private List<Goal> mExploreGoals;

    public DataSnapshotStore() {
    }

    public List<Idea> getIdeas() {
        if (mIdeas == null) {
            mIdeas = new ArrayList<>();
        }
        return mIdeas;
    }

    public void setIdeas(List<Idea> ideas) {
        mIdeas = ideas;
    }

    public List<Idea> getPendingIdeas() {
        if (mPendingIdeas == null) {
            mPendingIdeas = new ArrayList<>();
        }
        return mPendingIdeas;
    }

    public List<Idea> getSuggestions() {
        if (mSuggestions == null) {
            mSuggestions = new ArrayList<>();
        }
        return mSuggestions;
    }

    public List<Goal> getSavedGoals() {
        if (mSavedGoals == null) {
            mSavedGoals = new ArrayList<>();
        }
        return mSavedGoals;
    }

    public List<Goal> getExploreGoals() {
        if (mExploreGoals == null) {
            mExploreGoals = new ArrayList<>();
        }
        return mExploreGoals;
    }

}
