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
    List<Idea> mIdeaSuggestions;

    public IdeaSnapshotStore() {
        mIdeas = new ArrayList<>();
        mIdeaSuggestions = new ArrayList<>();
    }

    public IdeaSnapshotStore(List<Idea> ideas) {
        mIdeas = ideas;
        mIdeaSuggestions = new ArrayList<>();
    }

}
