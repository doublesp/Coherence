package com.doublesp.coherence.interfaces.api;

import com.doublesp.coherence.models.v1.RecipeResponse;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface EdamamApiEndpointInterface {

    String REQUEST_HEADER_WITH_CACHE = "Cache-Control: max-age=640000";

    @Headers({REQUEST_HEADER_WITH_CACHE})
    @GET("search")
    Observable<RecipeResponse> searchRecipe(@Query("q") String keyword,
            @Query("app_id") String appId, @Query("app_key") String appKey);

}
