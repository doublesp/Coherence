package com.doublesp.coherence.datastore;

import com.doublesp.coherence.viewmodels.Idea;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pinyaoting on 11/14/16.
 */

@Parcel
public class IdeaSnapshotStore {

    List<Idea> mIdeas;
    List<Idea> mSuggestions;

    public IdeaSnapshotStore() {
        mIdeas = new ArrayList<>();
        mSuggestions = new ArrayList<>();
    }

}
