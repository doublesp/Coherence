package com.doublesp.coherence.datastore;

import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parcel
public class DataSnapshotStore {

    private List<Idea> mIdeas;
    private Map<String, List<Idea>> mPendingIdeas;
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

    public Map<String, List<Idea>> getPendingIdeas() {
        if (mPendingIdeas == null) {
            mPendingIdeas = new HashMap<>();
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
