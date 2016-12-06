package com.doublesp.coherence.dependencies.modules.data;

import android.app.Application;

import com.doublesp.coherence.R;
import com.doublesp.coherence.api.EdamamClient;
import com.doublesp.coherence.api.SpoonacularClient;
import com.doublesp.coherence.interfaces.api.EdamamApiEndpointInterface;
import com.doublesp.coherence.interfaces.api.SpoonacularApiEndpointInterface;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
import com.doublesp.coherence.interfaces.data.RecipeV2RepositoryInterface;
import com.doublesp.coherence.interfaces.scopes.DataLayerScope;
import com.doublesp.coherence.repositories.EdamamRepository;
import com.doublesp.coherence.repositories.SpoonacularRepository;

import java.util.Map;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class DataLayerModule {

    @Provides
    @DataLayerScope
    public EdamamApiEndpointInterface providesRecipeApiEndpointInterface(
            Map<Integer, Retrofit> retrofitMap) {
        Retrofit retrofit = retrofitMap.get(R.id.idea_category_recipe);
        return retrofit.create(EdamamApiEndpointInterface.class);
    }

    @Provides
    @DataLayerScope
    public EdamamClient providesEdamamClient(EdamamApiEndpointInterface apiEndpointInterface) {
        return new EdamamClient(apiEndpointInterface);
    }

    @Provides
    @DataLayerScope
    public RecipeRepositoryInterface providesRecipeRepository(Application application,
            EdamamClient client) {
        return new EdamamRepository(application, client);
    }

    @Provides
    @DataLayerScope
    public SpoonacularApiEndpointInterface providesRecipeV2ApiEndpointInterface(
            Map<Integer, Retrofit> retrofitMap) {
        Retrofit retrofit = retrofitMap.get(R.id.idea_category_recipe_v2);
        return retrofit.create(SpoonacularApiEndpointInterface.class);
    }

    @Provides
    @DataLayerScope
    public SpoonacularClient providesSpoonacularClient(
            SpoonacularApiEndpointInterface apiEndpointInterface) {
        return new SpoonacularClient(apiEndpointInterface);
    }

    @Provides
    @DataLayerScope
    public RecipeV2RepositoryInterface providesRecipeV2Repository(Application application,
            SpoonacularClient client) {
        return new SpoonacularRepository(application, client);
    }
}
