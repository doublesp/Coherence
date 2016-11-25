package com.doublesp.coherence.viewmodels;


import java.util.HashMap;

public class UserList {
    private String listName;
    private String owner;
    private HashMap<String, Object> timestampCreated;

    public UserList() {
    }

    public UserList(String mListId, String owner, HashMap<String, Object> timestampCreated) {

        this.listName = mListId;
        this.owner = owner;
        this.timestampCreated = timestampCreated;
    }

    public String getlistName() {
        return listName;
    }

    public String getOwner() {
        return owner;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }
}
