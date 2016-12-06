package com.doublesp.coherence.viewmodels;

import com.doublesp.coherence.R;
import com.google.firebase.database.Exclude;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

@Parcel
public class Idea {

    String id;
    int category;
    String content;
    boolean crossedOut;
    int type; // R.id.idea_type_user_generated or R.id.idea_type_suggestion
    IdeaMeta meta;

    public Idea() {
    }

    public Idea(String id, int category, String content, boolean crossedOut, int type,
            IdeaMeta meta) {
        this.id = id;
        this.category = category;
        this.content = content;
        this.crossedOut = crossedOut;
        this.type = type;
        this.meta = meta;
    }

    public static Idea newInstanceOfBlankIdea() {
        Idea blankIdea = new Idea();
        blankIdea.type = R.id.idea_type_blank;
        return blankIdea;
    }

    public String getId() {
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

    public int getType() {
        return type;
    }

    public IdeaMeta getMeta() {
        return meta;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Idea) {
            Idea target = (Idea) obj;
            return this.content.equals(target.content);
        }
        return false;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("category", getCategory());
        result.put("content", getContent());
        result.put("crossedOut", isCrossedOut());
        result.put("type", getType());
        result.put("meta", getMeta());

        return result;
    }

}
