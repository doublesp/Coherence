package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.models.Ingredient;
import com.doublesp.coherence.models.Recipe;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaMeta;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Created by pinyaoting on 11/17/16.
 */

public class RecipeInteractor extends IdeaInteractorBase {

    IdeaDataStoreInterface mIdeaDataStore;
    RecipeRepositoryInterface mRecipeRepository;

    public RecipeInteractor(IdeaDataStoreInterface ideaDataStore,
                           RecipeRepositoryInterface recipeRepository) {
        super(ideaDataStore);
        mIdeaDataStore = ideaDataStore;
        mRecipeRepository = recipeRepository;
        mRecipeRepository.subscribe(new Observer<List<Recipe>>() {
            List<Recipe> mRecipes;
            @Override
            public void onCompleted() {
                List<Idea> ideas = new ArrayList<>();
                for (Recipe recipe : mRecipes) {
                    StringBuilder summaryBuilder = new StringBuilder();
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        summaryBuilder.append(ingredient.getText());
                        summaryBuilder.append("\n");
                    }
                    ideas.add(new Idea(recipe.getUri(),
                            R.id.idea_category_recipe,
                            recipe.getLabel(),
                            false,
                            R.id.idea_type_suggestion,
                            new IdeaMeta(recipe.getImageUrl(), recipe.getLabel(), summaryBuilder.toString())
                    ));
                }
                mIdeaDataStore.setSuggestions(ideas);
                mIdeaDataStore.setIdeaState(R.id.idea_state_loaded);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Recipe> recipes) {
                mRecipes = recipes;
            }
        });
    }

    @Override
    int getCategory() {
        return R.id.idea_category_recipe;
    }

    @Override
    public void getSuggestions(Idea idea) {
        String keyword = idea == null ? "chicken" : idea.getContent();
        mRecipeRepository.searchRecipe(keyword);
    }
}
