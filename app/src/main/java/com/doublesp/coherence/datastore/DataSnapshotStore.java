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

    List<Idea> mIdeas;
    List<Idea> mSuggestions;
    List<Goal> mSavedGoals;
    List<Goal> mGoals;

    public DataSnapshotStore() {
        mIdeas = new ArrayList<>();
        mSuggestions = new ArrayList<>();
        mSavedGoals = new ArrayList<>();
        mGoals = new ArrayList<>();
    }

}
