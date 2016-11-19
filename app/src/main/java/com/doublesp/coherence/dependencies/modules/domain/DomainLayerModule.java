package com.doublesp.coherence.dependencies.modules.domain;

import com.doublesp.coherence.R;
import com.doublesp.coherence.datastore.IdeaDataStore;
import com.doublesp.coherence.interactors.MockRecipeInteractor;
import com.doublesp.coherence.interactors.RecipeInteractor;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
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
    @IntKey(R.id.idea_category_recipe)
    public IdeaInteractorInterface providesRecipeIdeaInteractor(IdeaDataStoreInterface ideaDataStore,
                                                               RecipeRepositoryInterface recipeRepository) {
        return new RecipeInteractor(ideaDataStore, recipeRepository);
    }

    @Provides
    @DomainLayerScope
    @IntoMap
    @IntKey(R.id.idea_category_debug)
    public IdeaInteractorInterface providesMockRecipeIdeaInteractor(RecipeRepositoryInterface recipeRepository) {
        IdeaDataStore ideaDataStore = new IdeaDataStore();
        return new MockRecipeInteractor(ideaDataStore, recipeRepository);
    }

}
