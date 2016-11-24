package com.doublesp.coherence.models.v2;

import java.util.List;

/**
 * Created by pinyaoting on 11/22/16.
 */

public class RecipeResponseV2 {

    List<RecipeV2> products;
    int number;
    int offset;
    int totalProducts;

    public List<RecipeV2> getProducts() {
        return products;
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
}
