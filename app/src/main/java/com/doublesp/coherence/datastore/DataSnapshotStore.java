package com.doublesp.coherence.datastore;

import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pinyaoting on 11/14/16.
 */

@Parcel
public class DataSnapshotStore {

    private List<Idea> mIdeas;
    private List<Idea> mSuggestions;
    private List<Goal> mSavedGoals;
    private List<Goal> mGoals;

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

    public List<Goal> getGoals() {
        if (mGoals == null) {
            mGoals = new ArrayList<>();
        }
        return mGoals;
    }

}
