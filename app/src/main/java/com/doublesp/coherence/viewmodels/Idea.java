package com.doublesp.coherence.viewmodels;

import org.parceler.Parcel;

/**
 * Created by pinyaoting on 11/10/16.
 */

@Parcel
public class Idea {

    Long id;
    int category;
    String content;
    boolean crossedOut;
    IdeaMeta meta;

    public Idea() {
    }

    public Idea(Long id, int category, String content, boolean crossedOut) {
        this.id = id;
        this.category = category;
        this.content = content;
        this.crossedOut = crossedOut;
    }

    public Idea(Long id, int category, String content, boolean crossedOut, IdeaMeta meta) {
        this.id = id;
        this.category = category;
        this.content = content;
        this.crossedOut = crossedOut;
        this.meta = meta;
    }

    public Long getId() {
        return id;
    }

    public int getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public boolean isCrossedOut() {
        return crossedOut;
    }

    public IdeaMeta getMeta() {
        return meta;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Idea) {
            Idea target = (Idea)obj;
            return this.id == target.id;
        }
        return false;
    }
}
