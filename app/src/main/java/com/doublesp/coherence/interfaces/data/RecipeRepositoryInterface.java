package com.doublesp.coherence.interfaces.data;

import com.doublesp.coherence.models.Recipe;

import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/17/16.
 */

public interface RecipeRepositoryInterface {

    void subscribe(Observer<List<Recipe>> observer);

    void searchRecipe(String keyword);

}
