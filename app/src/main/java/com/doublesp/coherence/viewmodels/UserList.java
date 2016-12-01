package com.doublesp.coherence.viewmodels;


import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("listName", getlistName());
        result.put("owner", getOwner());
        result.put("timestampCreated", getTimestampCreated());

        return result;
    }
}
