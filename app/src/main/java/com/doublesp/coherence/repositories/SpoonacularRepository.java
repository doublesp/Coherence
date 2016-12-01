package com.doublesp.coherence.repositories;

import android.app.Application;

import com.doublesp.coherence.api.SpoonacularClient;
import com.doublesp.coherence.database.RecipeDatabase;
import com.doublesp.coherence.interfaces.data.RecipeV2RepositoryInterface;
import com.doublesp.coherence.models.v2.IngredientV2;
import com.doublesp.coherence.models.v2.RandomRecipeResponseV2;
import com.doublesp.coherence.models.v2.RecipeResponseV2;
import com.doublesp.coherence.models.v2.RecipeV2;
import com.doublesp.coherence.utils.NetworkUtils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by pinyaoting on 11/22/16.
 */

public class SpoonacularRepository implements RecipeV2RepositoryInterface {

    List<Observer<List<RecipeV2>>> mSubscribers;
    List<Observer<RecipeV2>> mDetailSubscribers;
    List<Observer<List<RecipeV2>>> mAutoCompleteRecipeSubscribers;
    List<Observer<List<IngredientV2>>> mAutoCompleteIngredientSubscribers;
    private Application mApplication;
    private SpoonacularClient mClient;

    public SpoonacularRepository(Application application, SpoonacularClient client) {
        mApplication = application;
        mClient = client;
        mSubscribers = new ArrayList<>();
        mDetailSubscribers = new ArrayList<>();
        mAutoCompleteRecipeSubscribers = new ArrayList<>();
        mAutoCompleteIngredientSubscribers = new ArrayList<>();
        getClient().subscribeRecipe(new Observer<RecipeResponseV2>() {
            List<RecipeV2> mRecipes = new ArrayList<RecipeV2>();

            @Override
            public void onCompleted() {
                notifyAllObservers(mRecipes);
            }

            @Override
            public void onError(Throwable e) {
                notifyAllObservers(null);
            }

            @Override
            public void onNext(RecipeResponseV2 recipeResponse) {
                mRecipes.clear();
                mRecipes.addAll(recipeResponse.getProducts());
                asyncPersistRecipes(mRecipes);
            }
        });
        getClient().subscribeRecipeByIngredients(new Observer<List<RecipeV2>>() {
            List<RecipeV2> mRecipes = new ArrayList<RecipeV2>();

            @Override
            public void onCompleted() {
                notifyAllObservers(mRecipes);
            }

            @Override
            public void onError(Throwable e) {
                notifyAllObservers(null);
            }

            @Override
            public void onNext(List<RecipeV2> recipes) {
                mRecipes.clear();
                mRecipes.addAll(recipes);
                asyncPersistRecipes(mRecipes);
            }
        });
        getClient().subscribeRandomRecipe(new Observer<RandomRecipeResponseV2>() {
            List<RecipeV2> mRecipes = new ArrayList<RecipeV2>();

            @Override
            public void onCompleted() {
                notifyAllObservers(mRecipes);
            }

            @Override
            public void onError(Throwable e) {
                notifyAllObservers(null);
            }

            @Override
            public void onNext(RandomRecipeResponseV2 randomRecipeResponseV2) {
                mRecipes.clear();
                mRecipes.addAll(randomRecipeResponseV2.getRecipes());
                asyncPersistRecipes(mRecipes);
            }
        });
        getClient().subscribeRecipeDetail(new Observer<RecipeV2>() {
            RecipeV2 mRecipe = null;

            @Override
            public void onCompleted() {
                notifyAllDetailObservers(mRecipe);
                List<RecipeV2> recipes = new ArrayList<RecipeV2>();
                recipes.add(mRecipe);
                asyncPersistRecipes(recipes);
            }

            @Override
            public void onError(Throwable e) {
                notifyAllDetailObservers(null);
            }

            @Override
            public void onNext(RecipeV2 recipeV2) {
                mRecipe = recipeV2;
            }
        });
        getClient().subscribeAutoCompleteIngredient(new Observer<List<IngredientV2>>() {
            List<IngredientV2> mIngredients = new ArrayList<IngredientV2>();

            @Override
            public void onCompleted() {
                notifyAllAutoCompleteIngredientObservers(mIngredients);
            }

            @Override
            public void onError(Throwable e) {
                notifyAllAutoCompleteIngredientObservers(null);
            }

            @Override
            public void onNext(List<IngredientV2> ingredients) {
                mIngredients.clear();
                mIngredients.addAll(ingredients);
            }
        });
        getClient().subscribeAutoCompleteRecipe(new Observer<List<RecipeV2>>() {
            List<RecipeV2> mRecipes = new ArrayList<RecipeV2>();

            @Override
            public void onCompleted() {
                notifyAllAutoCompleteRecipeObservers(mRecipes);
            }

            @Override
            public void onError(Throwable e) {
                notifyAllAutoCompleteRecipeObservers(null);
            }

            @Override
            public void onNext(List<RecipeV2> recipes) {
                mRecipes.clear();
                mRecipes.addAll(recipes);
            }
        });
    }

    protected SpoonacularClient getClient() {
        return mClient;
    }

    protected Application getApplication() {
        return mApplication;
    }

    @Override
    public void subscribe(Observer<List<RecipeV2>> observer) {
        mSubscribers.add(observer);
    }

    @Override
    public void subscribeDetail(Observer<RecipeV2> observer) {
        mDetailSubscribers.add(observer);
    }

    @Override
    public void subscribeAutoCompleteIngredient(Observer<List<IngredientV2>> observer) {
        mAutoCompleteIngredientSubscribers.add(observer);
    }

    @Override
    public void subscribeAutoCompleteRecipe(Observer<List<RecipeV2>> observer) {
        mAutoCompleteRecipeSubscribers.add(observer);
    }

    @Override
    public void searchRecipe(String keyword, int count, int offset) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            return;
        }
        getClient().searchRecipe(keyword, count, offset);
    }

    @Override
    public void searchRecipeByIngredients(
            String ingredients,
            boolean fillIngredients,
            int number,
            int ranking) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            return;
        }
        getClient().searchRecipeByIngredients(ingredients, fillIngredients, number, ranking);
    }

    @Override
    public void searchRecipeDetail(String id) {
        getClient().searchRecipeDetail(id);
    }

    @Override
    public void randomRecipe(int count) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            fallback();
            return;
        }
        getClient().randomRecipe(count);
    }

    @Override
    public void autoCompleteIngredients(String keyword, int count) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            return;
        }
        getClient().autoCompleteIngredient(keyword, count);
    }

    @Override
    public void autoCompleteRecipes(String keyword, int count) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            return;
        }
        getClient().autoCompleteRecipe(keyword, count);
    }

    void fallback() {
        notifyAllObservers(RecipeV2.recentItems());
    }

    void notifyAllObservers(List<RecipeV2> recipes) {
        ConnectableObservable<List<RecipeV2>> connectableObservable = Observable.just(
                recipes).publish();
        for (Observer<List<RecipeV2>> subscriber : mSubscribers) {
            connectableObservable.subscribeOn(Schedulers.immediate()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

    void notifyAllDetailObservers(RecipeV2 recipe) {
        ConnectableObservable<RecipeV2> connectableObservable = Observable.just(
                recipe).publish();
        for (Observer<RecipeV2> subscriber : mDetailSubscribers) {
            connectableObservable.subscribeOn(Schedulers.immediate()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

    void notifyAllAutoCompleteIngredientObservers(List<IngredientV2> ingredients) {
        ConnectableObservable<List<IngredientV2>> connectableObservable = Observable.just(
                ingredients).publish();
        for (Observer<List<IngredientV2>> subscriber : mAutoCompleteIngredientSubscribers) {
            connectableObservable.subscribeOn(Schedulers.immediate()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

    void notifyAllAutoCompleteRecipeObservers(List<RecipeV2> recipes) {
        ConnectableObservable<List<RecipeV2>> connectableObservable = Observable.just(
                recipes).publish();
        for (Observer<List<RecipeV2>> subscriber : mAutoCompleteRecipeSubscribers) {
            connectableObservable.subscribeOn(Schedulers.immediate()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

    void asyncPersistRecipes(List<RecipeV2> recipes) {
        // persists into database in background thread
        FlowManager.getDatabase(RecipeDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<RecipeV2>() {
                            @Override
                            public void processModel(RecipeV2 recipe) {
                                recipe.save();
                                if (recipe.getExtendedIngredients() != null) {
                                    for (IngredientV2 ingredient : recipe.getExtendedIngredients
                                            ()) {
                                        // TODO: save object relation
                                        ingredient.save();
                                    }
                                }
                            }
                        }).addAll(recipes).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {

                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {

                    }
                }).build().execute();
    }
}
