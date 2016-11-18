package com.doublesp.coherence.viewmodels;

import org.parceler.Parcel;

import java.util.List;
import java.util.UUID;

/**
 * Created by pinyaoting on 11/16/16.
 */

@Parcel
public class Plan {

    String id;
    List<Idea> ideas;
    String title;

    public Plan() {
    }

    public Plan(List<Idea> ideas, String title) {
        this.id = UUID.randomUUID().toString();
        this.ideas = ideas;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public List<Idea> getIdeas() {
        return ideas;
    }

    public String getTitle() {
        return title;
    }
}
