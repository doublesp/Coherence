package com.doublesp.coherence.interactors;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
import com.doublesp.coherence.interfaces.domain.DataStoreInterface;
import com.doublesp.coherence.models.v1.Recipe;
import com.doublesp.coherence.viewmodels.Goal;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

@Deprecated
public class RecipeInteractor extends IdeaInteractorBase {

    public static final long RECIPE_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES = 500;

    DataStoreInterface mIdeaDataStore;
    RecipeRepositoryInterface mRecipeRepository;
    PublishSubject<String> mSearchDebouner;

    public RecipeInteractor(DataStoreInterface ideaDataStore,
                            RecipeRepositoryInterface recipeRepository) {
        super(ideaDataStore);
        mIdeaDataStore = ideaDataStore;
        mRecipeRepository = recipeRepository;
        mRecipeRepository.subscribe(new Observer<List<Recipe>>() {
            List<Recipe> mRecipes;

            @Override
            public void onCompleted() {

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

    // NOTE: debounce is to prevent unnecessary network requests -- we only search for recipes after
    // user stopped typing
    private void searchRecipeWithDebounce(String keyword) {
        if (mSearchDebouner == null) {
            mSearchDebouner = PublishSubject.create();
            mSearchDebouner.debounce(RECIPE_INTERACTOR_DEBOUNCE_TIME_IN_MILLIES,
                    TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mRecipeRepository.searchRecipe(s);
                        }
                    });
        }
        mSearchDebouner.onNext(keyword);
    }

    @Override
    public void loadPendingIdeas(Goal goal) {
        return;
    }
}
