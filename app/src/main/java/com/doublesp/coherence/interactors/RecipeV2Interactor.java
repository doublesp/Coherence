package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.data.RecipeV2RepositoryInterface;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.models.v2.RecipeV2;
import com.doublesp.coherence.models.v2.SavedRecipe;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by pinyaoting on 11/26/16.
 */

public class RecipeV2Interactor implements GoalInteractorInterface {

    public static final int RECIPEV2_INTERACTOR_BATCH_SIZE = 10;
    public static final long RECIPEV2_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES = 500;
    public static final long RECIPEV2_BOOKMARK_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES = 100;
    public static final int RECIPEV2_INTERACTOR_SEARCH_BY_INGREDIENT_RANKING_MORE_HITS = 1;
    public static final int RECIPEV2_INTERACTOR_SEARCH_BY_INGREDIENT_RANKING_FEWER_MISSES = 2;
    public static final boolean RECIPEV2_INTERACTOR_SEARCH_BY_INGREDIENT_SHOW_INGREDIENTS = true;
    public static final boolean RECIPEV2_INTERACTOR_SEARCH_BY_INGREDIENT_HIDE_INGREDIENTS = false;
    static final int count = 10;
    static final int offset = 0;

    DataStoreInterface mDataStore;
    RecipeV2RepositoryInterface mRecipeRepository;
    PublishSubject<String> mSearchDebouncer;
    PublishSubject<Integer> mBookmarkDebouncer;
    PublishSubject<Integer> mSavedBookmarkDebouncer;

    public RecipeV2Interactor(DataStoreInterface dataStore,
                              RecipeV2RepositoryInterface recipeRepository) {
        mDataStore = dataStore;
        mRecipeRepository = recipeRepository;
        mRecipeRepository.subscribe(new Observer<List<RecipeV2>>() {
            List<RecipeV2> mRecipes = new ArrayList<RecipeV2>();

            @Override
            public void onCompleted() {
                List<Goal> goals = new ArrayList<>();
                for (RecipeV2 recipe : mRecipes) {
                    boolean isBookmarked = (SavedRecipe.byId(recipe.getId()) != null);
                    goals.add(new Goal(
                            recipe.getId(),
                            recipe.getTitle(),
                            recipe.getInstructions(),
                            recipe.getImage(),
                            isBookmarked
                    ));
                }
                mDataStore.setGoals(goals);
                mDataStore.setGoalState(R.id.state_loaded);
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
    }

    @Override
    public int getGoalCount() {
        return mDataStore.getGoalCount();
    }

    @Override
    public Goal getGoalAtPos(int pos) {
        return mDataStore.getGoalAtPos(pos);
    }

    @Override
    public void clearGoal() {
        mDataStore.clearGoals();
    }

    @Override
    public void bookmarkGoalAtPos(int pos) {
        getBookmarkDebouncer().onNext(pos);
    }

    @Override
    public void search(String keyword) {
        mDataStore.setGoalState(R.id.state_refreshing);
        if (keyword == null) {
            mRecipeRepository.randomRecipe(RECIPEV2_INTERACTOR_BATCH_SIZE);
            return;
        }
        searchRecipeWithDebounce(keyword);
    }

    @Override
    public void searchGoalByIdeas(List<Idea> ideas) {
        StringBuilder ingredientsBuilder = new StringBuilder();
        for (Idea idea : ideas) {
            ingredientsBuilder.append(idea.getContent());
            ingredientsBuilder.append(",");
        }
        ingredientsBuilder.deleteCharAt(ingredientsBuilder.lastIndexOf(","));
        mRecipeRepository.searchRecipeByIngredients(
                ingredientsBuilder.toString(),
                RECIPEV2_INTERACTOR_SEARCH_BY_INGREDIENT_HIDE_INGREDIENTS,
                RECIPEV2_INTERACTOR_BATCH_SIZE,
                RECIPEV2_INTERACTOR_SEARCH_BY_INGREDIENT_RANKING_FEWER_MISSES);
    }

    @Override
    public void subscribeToGoalStateChange(Observer<Integer> observer) {
        mDataStore.subscribeToGoalStateChanges(observer);
    }

    @Override
    public int getSavedGoalCount() {
        return mDataStore.getSavedGoalCount();
    }

    @Override
    public Goal getSavedGoalAtPos(int pos) {
        return mDataStore.getSavedGoalAtPos(pos);
    }

    @Override
    public void bookmarkSavedGoalAtPos(int pos) {
        getSavedBookmarkDebouncer().onNext(pos);
    }

    @Override
    public void loadBookmarkedGoals() {
        mDataStore.setSavedGoalState(R.id.state_refreshing);
        List<Goal> bookmarkedGoals = new ArrayList<>();
        List<RecipeV2> bookmarkedRecipes = SavedRecipe.savedRecipes();
        for (RecipeV2 recipe : bookmarkedRecipes) {
            bookmarkedGoals.add(new Goal(
                    recipe.getId(),
                    recipe.getTitle(),
                    recipe.getInstructions(),
                    recipe.getImage(),
                    true));
        }
        mDataStore.setSavedGoals(bookmarkedGoals);
        mDataStore.setSavedGoalState(R.id.state_loaded);
    }

    @Override
    public void subscribeToSavedGoalStateChange(Observer<Integer> observer) {
        mDataStore.subscribeToSavedGoalStateChanges(observer);
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

    private PublishSubject getBookmarkDebouncer() {
        if (mBookmarkDebouncer == null) {
            mBookmarkDebouncer = PublishSubject.create();
            mBookmarkDebouncer.debounce(RECIPEV2_BOOKMARK_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES,
                    TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer pos) {
                            mDataStore.setGoalState(R.id.state_refreshing);
                            Goal goal = mDataStore.getGoalAtPos(pos);
                            SavedRecipe savedRecipe;
                            if (!goal.isBookmarked()) {
                                savedRecipe = new SavedRecipe();
                                savedRecipe.setId(goal.getId());
                                savedRecipe.save();
                            } else {
                                savedRecipe = SavedRecipe.byId(goal.getId());
                                if (savedRecipe != null) {
                                    savedRecipe.delete();
                                }
                            }
                            mDataStore.updateGoal(pos, new Goal(
                                    goal.getId(),
                                    goal.getTitle(),
                                    goal.getDescription(),
                                    goal.getImageUrl(),
                                    !goal.isBookmarked()));
                            mDataStore.setGoalState(R.id.state_loaded);
                        }
                    });
        }
        return mBookmarkDebouncer;
    }

    private PublishSubject getSavedBookmarkDebouncer() {
        if (mSavedBookmarkDebouncer == null) {
            mSavedBookmarkDebouncer = PublishSubject.create();
            mSavedBookmarkDebouncer.debounce(RECIPEV2_BOOKMARK_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES,
                    TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer pos) {
                            mDataStore.setSavedGoalState(R.id.state_refreshing);
                            Goal goal = mDataStore.getSavedGoalAtPos(pos);
                            SavedRecipe savedRecipe;
                            if (!goal.isBookmarked()) {
                                savedRecipe = new SavedRecipe();
                                savedRecipe.setId(goal.getId());
                                savedRecipe.save();
                            } else {
                                savedRecipe = SavedRecipe.byId(goal.getId());
                                if (savedRecipe != null) {
                                    savedRecipe.delete();
                                }
                            }
                            mDataStore.updateSavedGoal(pos, new Goal(
                                    goal.getId(),
                                    goal.getTitle(),
                                    goal.getDescription(),
                                    goal.getImageUrl(),
                                    !goal.isBookmarked()));
                            mDataStore.setSavedGoalState(R.id.state_loaded);
                        }
                    });
        }
        return mSavedBookmarkDebouncer;
    }

    private void searchRecipeWithDebounce(final String keyword) {
        getDebouncer().onNext(keyword);
    }
}
