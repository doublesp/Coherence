package com.doublesp.coherence.dependencies.modules.domain;

import com.doublesp.coherence.R;
import com.doublesp.coherence.datastore.IdeaDataStore;
import com.doublesp.coherence.interactors.MovieInteractor;
import com.doublesp.coherence.interfaces.data.MovieRepositoryInterface;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.scopes.DomainLayerScope;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;

/**
 * Created by pinyaoting on 11/11/16.
 */

@Module
public class DomainLayerModule {

    @Provides
    @DomainLayerScope
    public IdeaDataStoreInterface providesIdeaDataStore() {
        return new IdeaDataStore();
    }

    @Provides
    @DomainLayerScope
    @IntoMap
    @IntKey(R.id.idea_category_movies)
    public IdeaInteractorInterface providesMovieIdeaInteractor(IdeaDataStoreInterface ideaDataStore,
            MovieRepositoryInterface movieRepository) {
        return new MovieInteractor(ideaDataStore, movieRepository);
    }

}
