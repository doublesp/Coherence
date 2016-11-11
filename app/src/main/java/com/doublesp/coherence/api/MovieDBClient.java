package com.doublesp.coherence.api;

import com.doublesp.coherence.interfaces.api.MoviesDBApiEndpointInterface;
import com.doublesp.coherence.models.MovieList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by pinyaoting on 11/10/16.
 */

public class MovieDBClient {

    static final int DELAY_BETWEEN_API_CALLS = 1000;
    static final String API_KEY = "4290a472ebb8f7ae1981ec25479e4087";
    MoviesDBApiEndpointInterface apiService;
    List<Observer<MovieList>> mSubscribers;

    public MovieDBClient(MoviesDBApiEndpointInterface apiService) {
        this.apiService = apiService;
    }

    public void getNowPlayingMovies() {
        Observable<MovieList> call = apiService.getNowPlayingMovies(API_KEY);
        asyncCall(call.publish());
    }

    public void getMovieTrailer(Long id) {
        Observable<MovieList> call = apiService.getMovieTrailer(id, API_KEY);
        asyncCall(call.publish());
    }

    public void subscribe(Observer subscriber) {
        mSubscribers.add(subscriber);
    }

    private void asyncCall(ConnectableObservable connectedObservable) {
        // TODO: remove this delay once we are confident we aren't making unneccesary api calls to this endpoint
        connectedObservable.delay(DELAY_BETWEEN_API_CALLS, TimeUnit.MILLISECONDS);
        for (Observer<MovieList> subscriber : mSubscribers) {
            connectedObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectedObservable.connect();
    }

}
