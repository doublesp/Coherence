package com.doublesp.coherence.interactors;

import com.google.common.base.Joiner;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.models.Ingredient;
import com.doublesp.coherence.models.Recipe;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by pinyaoting on 11/17/16.
 */

public class RecipeInteractor extends IdeaInteractorBase {

    public static final long RECIPE_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES = 500;

    IdeaDataStoreInterface mIdeaDataStore;
    RecipeRepositoryInterface mRecipeRepository;
    PublishSubject<String> mSearchDebouner;

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
                    List<Idea> relatedIdeas = new ArrayList<Idea>();
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        relatedIdeas.add(new Idea(recipe.getUri(), R.id.idea_category_recipe, ingredient.getFood(), false, R.id.idea_type_user_generated, null, null));
                    }
                    String description = Joiner.on("\n").skipNulls().join(recipe.getIngredientLines());
                    ideas.add(new Idea(recipe.getUri(),
                            R.id.idea_category_recipe,
                            recipe.getLabel(),
                            false,
                            R.id.idea_type_suggestion,
                            new IdeaMeta(recipe.getImageUrl(),
                                    recipe.getLabel(),
                                    description),
                            relatedIdeas
                    ));
                }
                mIdeaDataStore.setSuggestions(ideas);
                mIdeaDataStore.setIdeaState(R.id.idea_state_suggestion_loaded);
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
    public void getSuggestions(String keyword) {
        if (keyword == null) {
            keyword = "chicken"; // TODO: discover trending items instead of hard code keywords
        }
        searchRecipeWithDebounce(keyword);
    }

    @Override
    public String getSharableContent() {
        List<Idea> ideaList = mIdeaDataStore.getIdeas();
        StringBuilder sharableContentBuilder = new StringBuilder();
        for (Idea idea : ideaList) {
            sharableContentBuilder.append(idea.getContent());
            sharableContentBuilder.append("\n");
        }
        return sharableContentBuilder.toString();
    }

    // NOTE: debounce is to prevent unnecessary network requests -- we only search for recipes after
    // user stopped typing
    private void searchRecipeWithDebounce(String keyword) {
        if (mSearchDebouner == null) {
            mSearchDebouner = PublishSubject.create();
            mSearchDebouner.debounce(RECIPE_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mIdeaDataStore.setIdeaState(R.id.idea_state_refreshing);
                            mRecipeRepository.searchRecipe(s);
                        }
                    });
        }
        mSearchDebouner.onNext(keyword);
    }
}
