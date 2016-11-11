package com.doublesp.coherence.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pinyaoting on 10/14/16.
 */

public class MovieTrailerList {

    @SerializedName("youtube")
    public List<MovieTrailer> trailers;

}
