package com.doublesp.coherence.viewmodels;

/**
 * Created by pinyaoting on 12/5/16.
 */

public class IdeaReducer {

    Idea mIdea;

    public IdeaReducer(Idea idea) {
        mIdea = idea;
    }

    public Idea getIdea() {
        return mIdea;
    }

    public void setId(String id) {
        mIdea.id = id;
    }

    public void setCategory(int category) {
        mIdea.category = category;
    }

    public void setContent(String content) {
        mIdea.content = content;
    }

    public void setCrossedOut(boolean crossedOut) {
        mIdea.crossedOut = crossedOut;
    }

    public void setType(int type) {
        mIdea.type = type;
    }

    private IdeaMeta getMeta() {
        if (mIdea.meta == null) {
            mIdea.meta = new IdeaMeta();
        }
        return mIdea.meta;
    }

    public void setImageUrl(String imageUrl) {
        getMeta().imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        getMeta().title = title;
    }

    public void setDescription(String description) {
        getMeta().description = description;
    }
}
