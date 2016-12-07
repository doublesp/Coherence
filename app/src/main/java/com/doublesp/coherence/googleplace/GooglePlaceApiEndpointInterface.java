package com.doublesp.coherence.googleplace;


import com.doublesp.coherence.googleplace.gplace.GPlace;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sduan on 12/4/16.
 */

public interface GooglePlaceApiEndpointInterface {
    String REQUEST_HEADER_WITH_CACHE = "Cache-Control: max-age=640000";

    @Headers({REQUEST_HEADER_WITH_CACHE})
    @GET("nearbysearch/json?")
    Observable<GPlace> search(@Query("location") String latAndLong,
                              @Query("radius") String radius,
                              @Query("type") String type,
                              @Query("key") String key);

}
