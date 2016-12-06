package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.data.RecipeV2RepositoryInterface;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.models.v2.RecipeV2;
import com.doublesp.coherence.models.v2.SavedRecipe;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.GoalReducer;
import com.doublesp.coherence.viewmodels.Idea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

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

    public RecipeV2Interactor(DataStoreInterface dataStore,
            RecipeV2RepositoryInterface recipeRepository) {
        mDataStore = dataStore;
        mRecipeRepository = recipeRepository;
        mRecipeRepository.subscribe(new Observer<List<RecipeV2>>() {
            List<RecipeV2> mRecipes = new ArrayList<>();

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
                mDataStore.setExploreGoals(goals);
                mDataStore.setGoalState(new ViewState(
                        R.id.state_loaded, ViewState.OPERATION.RELOAD));
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
                GoalReducer exploreGoalReducer = mDataStore.getExploreGoalReducer(
                        mRecipe.getId());
                if (exploreGoalReducer != null) {
                    exploreGoalReducer.setDescription(mRecipe.getInstructions());
                }
                GoalReducer savedGoalReducer = mDataStore.getSavedGoalReducer(
                        mRecipe.getId());
                if (savedGoalReducer != null) {
                    savedGoalReducer.setDescription(mRecipe.getInstructions());
                }
                mDataStore.setGoalState(new ViewState(
                        R.id.state_loaded, ViewState.OPERATION.UPDATE));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RecipeV2 recipe) {
                mRecipe = recipe;
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
        mDataStore.setGoalState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.UPDATE, pos, 1));
        getBookmarkDebouncer().onNext(pos);
    }

    @Override
    public void search(String keyword) {
        mDataStore.setGoalState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.RELOAD));
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
    public void loadDetailsForGoalAtPos(int pos) {
        mDataStore.setGoalState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.UPDATE));
        Goal goal = mDataStore.getGoalAtPos(pos);
        mRecipeRepository.searchRecipeDetail(goal.getId());
    }

    @Override
    public void subscribeToGoalStateChange(Observer<ViewState> observer) {
        mDataStore.subscribeToGoalStateChanges(observer);
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
                            boolean isBookmarked = !goal.isBookmarked();
                            GoalReducer exploreGoalReducer = mDataStore.getExploreGoalReducer(
                                    goal.getId());
                            if (exploreGoalReducer != null) {
                                exploreGoalReducer.setBookmarked(isBookmarked);
                            }
                            GoalReducer savedGoalReducer = mDataStore.getSavedGoalReducer(
                                    goal.getId());
                            if (savedGoalReducer != null) {
                                savedGoalReducer.setBookmarked(isBookmarked);
                            }
                            mDataStore.setGoalState(new ViewState(
                                    R.id.state_loaded, ViewState.OPERATION.UPDATE, pos, 1));
                        }
                    });
        }
        return mBookmarkDebouncer;
    }

    private void searchRecipeWithDebounce(final String keyword) {
        getDebouncer().onNext(keyword);
    }

    @Override
    public int getDisplayGoalFlag() {
        return mDataStore.getGoalFlag();
    }

    @Override
    public void setDisplayGoalFlag(int flag) {
        mDataStore.setGoalFlag(flag);
        switch (flag) {
            case R.id.flag_saved_recipes:
                loadBookmarkedGoals();
        }
    }

    private void loadBookmarkedGoals() {
        mDataStore.setGoalState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.RELOAD));
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
        mDataStore.setGoalState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.RELOAD));
    }
}
