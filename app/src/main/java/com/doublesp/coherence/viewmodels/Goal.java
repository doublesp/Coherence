package com.doublesp.coherence.viewmodels;

import org.parceler.Parcel;

@Parcel
public class Goal {

    String id;
    String title;
    String description;
    String imageUrl;
    boolean bookmarked;

    public Goal() {
    }

    public Goal(String id, String title, String description, String imageUrl, boolean bookmarked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.bookmarked = bookmarked;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Goal) {
            Goal target = (Goal) obj;
            return this.getId().equals(target.getId());
        }
        return false;
    }
}
