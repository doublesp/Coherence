package com.doublesp.coherence.interfaces.data;

import com.doublesp.coherence.models.Movie;

import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface MovieRepositoryInterface {

    void subscribe(Observer<List<Movie>> observer);
    void getNowPlayingMovies();
    void getMovieTrailer(Long id);
}
