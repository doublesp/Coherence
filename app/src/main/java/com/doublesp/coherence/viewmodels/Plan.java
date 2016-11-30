package com.doublesp.coherence.viewmodels;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pinyaoting on 11/16/16.
 */

@Parcel
public class Plan {

    String id;
    List<Idea> ideas;
    String title;
    String owner;

    public Plan() {
    }

    public Plan(String id, List<Idea> ideas, String title, String owner) {
        this.id = id;
        this.ideas = (ideas == null) ? new ArrayList<Idea>() : ideas;
        this.title = title;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public List<Idea> getIdeas() {
        if (ideas == null) {
            ideas = new ArrayList<Idea>();
        }
        return ideas;
    }

    public String getTitle() {
        return title;
    }

    public String getOwner() {
        return owner;
    }
}
