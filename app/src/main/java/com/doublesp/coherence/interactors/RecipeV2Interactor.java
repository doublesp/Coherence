package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.data.RecipeV2RepositoryInterface;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.models.v2.IngredientV2;
import com.doublesp.coherence.models.v2.RecipeV2;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by pinyaoting on 11/22/16.
 */

public class RecipeV2Interactor extends IdeaInteractorBase {

    public static final long RECIPEV2_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES = 500;
    static final int count = 10;
    static final int offset = 0;

    IdeaDataStoreInterface mIdeaDataStore;
    RecipeV2RepositoryInterface mRecipeRepository;
    PublishSubject<String> mSearchDebouncer;

    public RecipeV2Interactor(IdeaDataStoreInterface ideaDataStore,
                              RecipeV2RepositoryInterface recipeRepository) {
        super(ideaDataStore);
        mIdeaDataStore = ideaDataStore;
        mRecipeRepository = recipeRepository;
        mRecipeRepository.subscribe(new Observer<List<RecipeV2>>() {
            List<RecipeV2> mRecipes = new ArrayList<RecipeV2>();

            @Override
            public void onCompleted() {
                List<Idea> ideas = new ArrayList<>();
                for (RecipeV2 recipe : mRecipes) {
                    List<Idea> relatedIdeas = new ArrayList<Idea>();
                    if (recipe.getExtendedIngredients() != null) {
                        for (IngredientV2 ingredient : recipe.getExtendedIngredients()) {
                            relatedIdeas.add(new Idea(ingredient.getId(), R.id.idea_category_recipe,
                                    ingredient.getName(), false, R.id.idea_type_user_generated, new IdeaMeta(ingredient.getImage(), ingredient.getName(), ingredient.getOriginalString()),
                                    null));
                        }
                    }
                    ideas.add(new Idea(recipe.getId(),
                            R.id.idea_category_recipe,
                            recipe.getTitle(),
                            false,
                            R.id.idea_type_suggestion,
                            new IdeaMeta(recipe.getImage(),
                                    recipe.getTitle(),
                                    recipe.getInstructions()),
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
            public void onNext(List<RecipeV2> recipes) {
                mRecipes.clear();
                mRecipes.addAll(recipes);
            }
        });
        mRecipeRepository.subscribeDetail(new Observer<RecipeV2>() {
            RecipeV2 mRecipe;

            @Override
            public void onCompleted() {
                List<Idea> relatedIdeas = new ArrayList<Idea>();
                if (mRecipe.getExtendedIngredients() != null) {
                    for (IngredientV2 ingredient : mRecipe.getExtendedIngredients()) {
                        relatedIdeas.add(new Idea(ingredient.getId(), R.id.idea_category_recipe,
                                ingredient.getName(), false, R.id.idea_type_user_generated, new IdeaMeta(ingredient.getImage(), ingredient.getName(), ingredient.getOriginalString()),
                                null));
                    }
                }
                Idea idea = new Idea(mRecipe.getId(),
                        R.id.idea_category_recipe,
                        mRecipe.getTitle(),
                        false,
                        R.id.idea_type_suggestion,
                        new IdeaMeta(mRecipe.getImage(),
                                mRecipe.getTitle(),
                                mRecipe.getInstructions()),
                        relatedIdeas
                );
                mIdeaDataStore.addIdea(idea);
                mIdeaDataStore.setIdeaState(R.id.idea_state_idea_loaded);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RecipeV2 recipeV2) {
                mRecipe = recipeV2;
            }
        });
    }

    @Override
    public void acceptSuggestedIdeaAtPos(int pos) {
        mIdeaDataStore.setIdeaState(R.id.idea_state_refreshing);
        Idea idea = mIdeaDataStore.getIdeaAtPos(pos);
        mIdeaDataStore.removeIdea(pos);
        mRecipeRepository.searchRecipeDetail(idea.getId());
    }

    @Override
    int getCategory() {
        return R.id.idea_category_recipe_v2;
    }

    @Override
    public void getSuggestions(String keyword) {
        if (keyword == null) {
            mIdeaDataStore.setIdeaState(R.id.idea_state_refreshing);
            mRecipeRepository.randomRecipe(10);
            return;
        }
        searchRecipeWithDebounce(keyword);
    }

    @Override
    public String getSharableContent() {
        return null;
    }

    private PublishSubject getDebouncer() {
        if (mSearchDebouncer == null) {
            mSearchDebouncer = PublishSubject.create();
            mSearchDebouncer.debounce(RECIPEV2_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES,
                    TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mIdeaDataStore.setIdeaState(R.id.idea_state_refreshing);
                            mRecipeRepository.searchRecipe(s, count, offset);
                        }
                    });
        }
        return mSearchDebouncer;
    }

    private void searchRecipeWithDebounce(final String keyword) {
        getDebouncer().onNext(keyword);
    }
}
