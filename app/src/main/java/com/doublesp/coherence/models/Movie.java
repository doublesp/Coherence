package com.doublesp.coherence.models;

import com.doublesp.coherence.database.MovieDatabase;
import com.doublesp.coherence.utils.URLFactory;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by pinyaoting on 10/10/16.
 */

@Table(database = MovieDatabase.class)
public class Movie extends BaseModel {

    @PrimaryKey
    @Column
    Long id;

    @Column
    String posterPath;

    @Column
    String backdropPath;

    @Column
    String originalTitle;

    @Column
    String overview;

    @SerializedName("vote_average")
    @Column
    double rating;

    @Column
    double popularity;

    public static Movie byId(long id) {
        return new Select().from(Movie.class).where(Movie_Table.id.eq(id)).querySingle();
    }

    public static List<Movie> recentItems() {
        return new Select().from(Movie.class).orderBy(Movie_Table.id, false).limit(50).queryList();
    }

    public Long getId() {
        return id;
    }

    public String getPosterPath() {
        return URLFactory.getPosterRequestURL(posterPath);
    }

    public String getBackdropPath() {
        return URLFactory.getBackdropRequestURL(backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public double getPopularity() {
        return popularity;
    }

    public boolean isHighRated() {
        return rating > 5;
    }
}
