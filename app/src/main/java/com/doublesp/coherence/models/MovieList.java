package com.doublesp.coherence.models;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by pinyaoting on 10/12/16.
 */

public class MovieList extends BaseModel {

    @SerializedName("results")
    public List<Movie> movies;

}
