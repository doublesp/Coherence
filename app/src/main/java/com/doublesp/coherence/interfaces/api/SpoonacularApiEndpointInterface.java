package com.doublesp.coherence.interfaces.api;

import com.doublesp.coherence.models.v2.IngredientV2;
import com.doublesp.coherence.models.v2.RandomRecipeResponseV2;
import com.doublesp.coherence.models.v2.RecipeResponseV2;
import com.doublesp.coherence.models.v2.RecipeV2;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by pinyaoting on 11/22/16.
 */

public interface SpoonacularApiEndpointInterface {

    String REQUEST_HEADER_WITH_CACHE = "Cache-Control: max-age=640000";
    String REQUEST_HEADER_API_KEY =
            "X-Mashape-Key: C63FSizlZ6mshv49TJt4OapjoeqOp1nSif7jsnz0CoPVIinGat";

    @Headers({REQUEST_HEADER_WITH_CACHE, REQUEST_HEADER_API_KEY})
    @GET("food/products/search")
    Observable<RecipeResponseV2> searchRecipe(@Query("query") String keyword,
                                              @Query("number") int number,
                                              @Query("offset") int offset);

    @Headers({REQUEST_HEADER_WITH_CACHE, REQUEST_HEADER_API_KEY})
    @GET("recipes/{id}/information")
    Observable<RecipeV2> searchRecipeDetail(@Path("id") String id);

    @Headers({REQUEST_HEADER_WITH_CACHE, REQUEST_HEADER_API_KEY})
    @GET("recipes/random")
    Observable<RandomRecipeResponseV2> randomRecipe(@Query("number") int number);

    @Headers({REQUEST_HEADER_WITH_CACHE, REQUEST_HEADER_API_KEY})
    @GET("food/ingredients/autocomplete")
    Observable<List<IngredientV2>> autocompleteIngredient(@Query("query") String keyword,
                                                          @Query("number") int number);

    @Headers({REQUEST_HEADER_WITH_CACHE, REQUEST_HEADER_API_KEY})
    @GET("recipes/autocomplete")
    Observable<List<RecipeV2>> autocompleteRecipe(@Query("query") String keyword,
                                                  @Query("number") int number);

    @Headers({REQUEST_HEADER_WITH_CACHE, REQUEST_HEADER_API_KEY})
    @GET("recipes/findByIngredients")
    Observable<List<RecipeV2>> searchRecipeByIngredients(
            @Query("ingredients") String ingredients,
            @Query("fillIngredients") boolean fillIngredients,
            @Query("number") int number,
            @Query("ranking") int ranking);
}
