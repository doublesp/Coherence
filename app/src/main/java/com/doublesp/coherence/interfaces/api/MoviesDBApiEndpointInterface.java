package com.doublesp.coherence.interfaces.api;

import com.doublesp.coherence.models.MovieList;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface MoviesDBApiEndpointInterface {

    String REQUEST_HEADER_WITH_CACHE = "Cache-Control: max-age=640000";

    @Headers({REQUEST_HEADER_WITH_CACHE})
    @GET("https://api.themoviedb.org/3/movie/now_playing")
    Observable<MovieList> getNowPlayingMovies(@Query("api_key") String apiKey);

    @Headers({REQUEST_HEADER_WITH_CACHE})
    @GET("https://api.themoviedb.org/3/movie/{id}/trailers")
    Observable<MovieList> getMovieTrailer(@Path("id") Long id, @Query("api_key") String apiKey);

}
