package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.data.RecipeV2RepositoryInterface;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.models.v2.IngredientV2;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.IdeaMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class IngredientInteractor extends IdeaInteractorBase {

    public static final int INGREDIENT_INTERACTOR_BATCH_SIZE = 10;
    public static final long INGREDIENT_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES = 500;
    static final int count = 10;
    static final int offset = 0;

    DataStoreInterface mDataStore;
    RecipeV2RepositoryInterface mRecipeRepository;
    PublishSubject<String> mSearchDebouncer;

    public IngredientInteractor(DataStoreInterface ideaDataStore,
                                RecipeV2RepositoryInterface recipeRepository) {
        super(ideaDataStore);
        mDataStore = ideaDataStore;
        mRecipeRepository = recipeRepository;
        mRecipeRepository.subscribeAutoCompleteIngredient(new Observer<List<IngredientV2>>() {
            List<IngredientV2> mIngredients = new ArrayList<>();

            @Override
            public void onCompleted() {
                List<Idea> suggestions = new ArrayList<>();
                for (IngredientV2 ingredient : mIngredients) {
                    Idea idea = new Idea(ingredient.getId(),
                            R.id.idea_category_recipe_v2,
                            ingredient.getName(),
                            false, R.id.idea_type_suggestion,
                            new IdeaMeta(
                                    ConstantsAndUtils.getSpoonacularImageUrl(ingredient.getImage()),
                                    ingredient.getName(),
                                    null));
                    suggestions.add(idea);
                }
                mDataStore.setSuggestions(suggestions);
                mDataStore.setSuggestionState(new ViewState(
                        R.id.state_loaded, ViewState.OPERATION.RELOAD));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<IngredientV2> ingredientV2s) {
                mIngredients.clear();
                mIngredients.addAll(ingredientV2s);
            }
        });
    }

    @Override
    int getCategory() {
        return R.id.idea_category_recipe_v2;
    }

    @Override
    public void getSuggestions(String keyword) {
        if (keyword == null) {
            // TODO: show default suggestions
            return;
        }
        mDataStore.setSuggestionState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.RELOAD));
        searchIngredientsWithDebounce(keyword);
    }

    @Override
    public void loadPendingIdeas(Goal goal) {
        mDataStore.setIdeaState(new ViewState(
                R.id.state_refreshing, ViewState.OPERATION.RELOAD));
        mDataStore.loadPendingIdeas(goal.getId());
        mDataStore.setIdeaState(new ViewState(
                R.id.state_loaded, ViewState.OPERATION.RELOAD));
    }

    private PublishSubject getDebouncer() {
        if (mSearchDebouncer == null) {
            mSearchDebouncer = PublishSubject.create();
            mSearchDebouncer.debounce(INGREDIENT_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES,
                    TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mRecipeRepository.autoCompleteIngredients(s,
                                    INGREDIENT_INTERACTOR_BATCH_SIZE, true);
                            mDataStore.setSuggestionState(new ViewState(
                                    R.id.state_loaded, ViewState.OPERATION.RELOAD));
                        }
                    });
        }
        return mSearchDebouncer;
    }

    private void searchIngredientsWithDebounce(final String keyword) {
        getDebouncer().onNext(keyword);
    }
}
