package com.doublesp.coherence.viewmodels;

import org.parceler.Parcel;

/**
 * Created by pinyaoting on 11/11/16.
 */

@Parcel
public class IdeaMeta {

    String imageUrl;
    String description;

    public IdeaMeta() {
    }

    public IdeaMeta(String imageUrl, String description) {
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
