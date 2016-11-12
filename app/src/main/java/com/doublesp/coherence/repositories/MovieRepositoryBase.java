package com.doublesp.coherence.repositories;

import android.app.Application;

import com.doublesp.coherence.api.MovieDBClient;

/**
 * Created by pinyaoting on 11/10/16.
 */

abstract public class MovieRepositoryBase {

    private Application mApplication;
    private MovieDBClient mClient;

    public MovieRepositoryBase(Application application, MovieDBClient client) {
        mApplication = application;
        mClient = client;
    }

    protected MovieDBClient getClient() {
        return mClient;
    }

    protected Application getApplication() {
        return mApplication;
    }

}
