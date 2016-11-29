package com.doublesp.coherence.interfaces.data;

import com.doublesp.coherence.models.v2.IngredientV2;
import com.doublesp.coherence.models.v2.RecipeV2;

import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/22/16.
 */

public interface RecipeV2RepositoryInterface {

    void subscribe(Observer<List<RecipeV2>> observer);
    void subscribeDetail(Observer<RecipeV2> observer);
    void subscribeAutoCompleteIngredient(Observer<List<IngredientV2>> observer);
    void subscribeAutoCompleteRecipe(Observer<List<RecipeV2>> observer);
    void searchRecipe(String keyword, int count, int offset);
    void searchRecipeByIngredients(
            String ingredients,
            boolean fillIngredients,
            int number,
            int ranking);
    void searchRecipeDetail(String id);
    void autoCompleteIngredients(String keyword, int count);
    void autoCompleteRecipes(String keyword, int count);
    void randomRecipe(int count);
}
