package com.doublesp.coherence.models.v2;

import java.util.List;

/**
 * Created by pinyaoting on 11/22/16.
 */

public class RecipeResponseV2 {

    List<RecipeV2> results;
    int number;
    int offset;
    int totalProducts;
    String baseUri;

    public List<RecipeV2> getResults() {
        return results;
    }

    public int getNumber() {
        return number;
    }

    public int getOffset() {
        return offset;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public String getBaseUri() {
        return baseUri;
    }
}
