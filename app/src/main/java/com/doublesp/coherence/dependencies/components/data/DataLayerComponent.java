package com.doublesp.coherence.dependencies.components.data;

import com.doublesp.coherence.dependencies.components.application.ApplicationComponent;
import com.doublesp.coherence.dependencies.modules.data.DataLayerModule;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
import com.doublesp.coherence.interfaces.data.RecipeV2RepositoryInterface;
import com.doublesp.coherence.interfaces.scopes.DataLayerScope;

import dagger.Component;

/**
 * Created by pinyaoting on 11/11/16.
 */

@DataLayerScope
@Component(dependencies = {ApplicationComponent.class}, modules = DataLayerModule.class)
public interface DataLayerComponent {

    RecipeRepositoryInterface recipeRepository();
    RecipeV2RepositoryInterface recipeV2Repository();

}
