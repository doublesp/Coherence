package com.doublesp.coherence.dependencies.modules.data;

import android.app.Application;

import com.doublesp.coherence.api.MovieDBClient;
import com.doublesp.coherence.interfaces.api.GoogleMapApiEndpointInterface;
import com.doublesp.coherence.interfaces.api.MoviesDBApiEndpointInterface;
import com.doublesp.coherence.interfaces.api.YelpApiEndpointInterface;
import com.doublesp.coherence.interfaces.data.MovieRepositoryInterface;
import com.doublesp.coherence.interfaces.scopes.DataLayerScope;
import com.doublesp.coherence.repositories.MoviesRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by pinyaoting on 11/11/16.
 */

@Module
public class DataLayerModule {

    @Provides
    @DataLayerScope
    public MoviesDBApiEndpointInterface providesMoviesDBApiEndpointInterface(Retrofit retrofit) {
        return retrofit.create(MoviesDBApiEndpointInterface.class);
    }

    @Provides
    @DataLayerScope
    public GoogleMapApiEndpointInterface providesGoogleMapApiEndpointInterface(Retrofit retrofit) {
        return retrofit.create(GoogleMapApiEndpointInterface.class);
    }

    @Provides
    @DataLayerScope
    public YelpApiEndpointInterface providesYelpApiEndpointInterface(Retrofit retrofit) {
        return retrofit.create(YelpApiEndpointInterface.class);
    }

    @Provides
    @DataLayerScope
    public MovieDBClient providesMovieDBClient(MoviesDBApiEndpointInterface apiEndpointInterface) {
        return new MovieDBClient(apiEndpointInterface);
    }

    @Provides
    @DataLayerScope
    public MovieRepositoryInterface providesMovieRepositoryInterface(Application application, MovieDBClient client) {
        return new MoviesRepository(application, client);
    }

}
