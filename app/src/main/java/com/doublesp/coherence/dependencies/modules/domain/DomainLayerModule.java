package com.doublesp.coherence.dependencies.modules.domain;

import android.content.Context;

import com.doublesp.coherence.R;
import com.doublesp.coherence.datastore.DataStore;
import com.doublesp.coherence.interactors.IngredientInteractor;
import com.doublesp.coherence.interactors.MockRecipeInteractor;
import com.doublesp.coherence.interactors.RecipeInteractor;
import com.doublesp.coherence.interactors.RecipeV2Interactor;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
import com.doublesp.coherence.interfaces.data.RecipeV2RepositoryInterface;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.scopes.DomainLayerScope;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;

@Module
public class DomainLayerModule {

    private static Context mContext;

    public DomainLayerModule(Context context) {
        mContext = context;
    }

    @Provides
    @DomainLayerScope
    public DataStoreInterface providesIdeaDataStore() {
        return new DataStore(mContext);
    }

    @Provides
    @DomainLayerScope
    @IntoMap
    @IntKey(R.id.idea_category_recipe)
    public com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface
    providesRecipeIdeaInteractor(
            DataStoreInterface ideaDataStore,
            RecipeRepositoryInterface recipeRepository) {
        return new RecipeInteractor(ideaDataStore, recipeRepository);
    }

    @Provides
    @DomainLayerScope
    @IntoMap
    @IntKey(R.id.idea_category_recipe_v2)
    public com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface
    providesRecipeV2IdeaInteractor(
            DataStoreInterface ideaDataStore,
            RecipeV2RepositoryInterface recipeRepository) {
        return new IngredientInteractor(ideaDataStore, recipeRepository);
    }

    @Provides
    @DomainLayerScope
    @IntoMap
    @IntKey(R.id.idea_category_debug)
    public com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface
    providesMockRecipeIdeaInteractor(
            RecipeRepositoryInterface recipeRepository) {
        DataStore ideaDataStore = new DataStore(mContext);
        return new MockRecipeInteractor(ideaDataStore, recipeRepository);
    }

    @Provides
    @DomainLayerScope
    public GoalInteractorInterface providesIdeaSearchInteractor(DataStoreInterface ideaDataStore,
            RecipeV2RepositoryInterface recipeRepository) {
        return new RecipeV2Interactor(ideaDataStore, recipeRepository);
    }

}
