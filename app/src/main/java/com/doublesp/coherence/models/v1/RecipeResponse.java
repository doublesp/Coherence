package com.doublesp.coherence.models.v1;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pinyaoting on 11/17/16.
 */

public class RecipeResponse {

    @SerializedName("hits")
    private List<RecipeResponseHit> responses;

    public List<RecipeResponseHit> getResponses() {
        return responses;
    }
}
