package com.doublesp.coherence.viewmodels;

import org.parceler.Parcel;

@Parcel
public class IdeaMeta {

    String imageUrl;
    String title;
    String description;

    public IdeaMeta() {
    }

    public IdeaMeta(String imageUrl, String title, String description) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
