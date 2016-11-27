package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.data.RecipeV2RepositoryInterface;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaSearchInteractorInterface;
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
 * Created by pinyaoting on 11/26/16.
 */

public class RecipeV2Interactor implements IdeaSearchInteractorInterface {

    public static final int RECIPEV2_INTERACTOR_BATCH_SIZE = 10;
    public static final long RECIPEV2_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES = 500;
    static final int count = 10;
    static final int offset = 0;

    IdeaDataStoreInterface mIdeaDataStore;
    RecipeV2RepositoryInterface mRecipeRepository;
    PublishSubject<String> mSearchDebouncer;

    public RecipeV2Interactor(IdeaDataStoreInterface ideaDataStore,
                                RecipeV2RepositoryInterface recipeRepository) {
        mIdeaDataStore = ideaDataStore;
        mRecipeRepository = recipeRepository;
        mRecipeRepository.subscribe(new Observer<List<RecipeV2>>() {
            List<RecipeV2> mRecipes = new ArrayList<RecipeV2>();

            @Override
            public void onCompleted() {
                List<Idea> ideas = new ArrayList<>();
                for (RecipeV2 recipe : mRecipes) {
                    ideas.add(new Idea(recipe.getId(),
                            R.id.idea_category_recipe,
                            recipe.getTitle(),
                            false,
                            R.id.idea_type_suggestion,
                            new IdeaMeta(recipe.getImage(),
                                    recipe.getTitle(),
                                    recipe.getInstructions())
                    ));
                }
                mIdeaDataStore.setSuggestions(ideas);
                mIdeaDataStore.setSuggestionState(R.id.suggestion_state_loaded);
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
                Idea idea = new Idea(mRecipe.getId(),
                        R.id.idea_category_recipe,
                        mRecipe.getTitle(),
                        false,
                        R.id.idea_type_suggestion,
                        new IdeaMeta(mRecipe.getImage(),
                                mRecipe.getTitle(),
                                mRecipe.getInstructions())
                );
                mIdeaDataStore.addIdea(idea);
                mIdeaDataStore.setSuggestionState(R.id.suggestion_state_loaded);
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
    public int getResultCount() {
        return mIdeaDataStore.getSuggestionCount();
    }

    @Override
    public Idea getResultAtPos(int pos) {
        return mIdeaDataStore.getSuggestionAtPos(pos);
    }

    @Override
    public void acceptResultAtPos(int pos) {
        Idea idea = mIdeaDataStore.getSuggestionAtPos(pos);
        // TODO: save the recipe locally
    }

    @Override
    public void search(String keyword) {
        mIdeaDataStore.setSuggestionState(R.id.suggestion_state_refreshing);
        if (keyword == null) {
            mRecipeRepository.randomRecipe(RECIPEV2_INTERACTOR_BATCH_SIZE);
            return;
        }
        searchRecipeWithDebounce(keyword);
    }

    private PublishSubject getDebouncer() {
        if (mSearchDebouncer == null) {
            mSearchDebouncer = PublishSubject.create();
            mSearchDebouncer.debounce(RECIPEV2_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES,
                    TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
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
