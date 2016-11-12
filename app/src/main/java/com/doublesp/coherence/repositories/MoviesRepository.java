package com.doublesp.coherence.repositories;

import android.app.Application;

import com.doublesp.coherence.api.MovieDBClient;
import com.doublesp.coherence.database.MovieDatabase;
import com.doublesp.coherence.interfaces.data.MovieRepositoryInterface;
import com.doublesp.coherence.models.Movie;
import com.doublesp.coherence.models.MovieList;
import com.doublesp.coherence.utils.NetworkUtils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.List;

import rx.Observer;
import rx.subjects.PublishSubject;

/**
 * Created by pinyaoting on 11/10/16.
 */

public class MoviesRepository extends MovieRepositoryBase implements MovieRepositoryInterface {

    PublishSubject<List<Movie>> mPublisher;

    public MoviesRepository(Application application, MovieDBClient client) {
        super(application, client);
        mPublisher = PublishSubject.create();
        getClient().subscribe(new Observer<MovieList>() {
            @Override
            public void onCompleted() {
                mPublisher.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                mPublisher.onError(e);
            }

            @Override
            public void onNext(MovieList movieList) {
                mPublisher.onNext(movieList.movies);
                // persists into database in background thread
                FlowManager.getDatabase(MovieDatabase.class)
                        .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<MovieList>() {
                                    @Override
                                    public void processModel(MovieList movieList) {
                                        for (Movie movie : movieList.movies) {
                                            movie.save();
                                        }
                                        ;
                                    }
                                }).add(movieList).build())
                        .error(new Transaction.Error() {
                            @Override
                            public void onError(Transaction transaction, Throwable error) {

                            }
                        })
                        .success(new Transaction.Success() {
                            @Override
                            public void onSuccess(Transaction transaction) {

                            }
                        }).build().execute();
            }
        });
    }

    @Override
    public void subscribe(Observer<List<Movie>> observer) {
        mPublisher.subscribe(observer);
    }

    @Override
    public void getNowPlayingMovies() {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            fallback();
            return;
        }
        getClient().getNowPlayingMovies();
    }

    @Override
    public void getMovieTrailer(Long id) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            return;
        }
        getClient().getMovieTrailer(id);
    }

    void fallback() {
        mPublisher.onNext(Movie.recentItems());
    }

}
