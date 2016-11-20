package com.doublesp.coherence.repositories;

import com.doublesp.coherence.api.EdamamClient;
import com.doublesp.coherence.database.RecipeDatabase;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
import com.doublesp.coherence.models.Ingredient;
import com.doublesp.coherence.models.Recipe;
import com.doublesp.coherence.models.RecipeResponse;
import com.doublesp.coherence.models.RecipeResponseHit;
import com.doublesp.coherence.utils.NetworkUtils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by pinyaoting on 11/17/16.
 */

public class EdamamRepository implements RecipeRepositoryInterface {

    private Application mApplication;
    private EdamamClient mClient;
    List<Observer<List<Recipe>>> mSubscribers;


    public EdamamRepository(Application application, EdamamClient client) {
        mApplication = application;
        mClient = client;
        mSubscribers = new ArrayList<>();
        getClient().subscribe(new Observer<RecipeResponse>() {
            List<Recipe> mRecipes = new ArrayList<Recipe>();
            @Override
            public void onCompleted() {
                notifyAllObservers(mRecipes);
            }

            @Override
            public void onError(Throwable e) {
                notifyAllObservers(null);
            }

            @Override
            public void onNext(RecipeResponse recipeResponse) {
                mRecipes.clear();
                for (RecipeResponseHit hit : recipeResponse.getResponses()) {
                    mRecipes.add(hit.getRecipe());
                }
                // persists into database in background thread
                FlowManager.getDatabase(RecipeDatabase.class)
                        .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<Recipe>() {
                                    @Override
                                    public void processModel(Recipe recipe) {
                                        recipe.save();
                                        for (Ingredient ingredient : recipe.getIngredients()) {
                                            ingredient.setUri(recipe.getUri());
                                            ingredient.save();
                                        }
                                    }
                                }).addAll(mRecipes).build())
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
        });
    }

    protected EdamamClient getClient() {
        return mClient;
    }

    protected Application getApplication() {
        return mApplication;
    }

    @Override
    public void subscribe(Observer<List<Recipe>> observer) {
        mSubscribers.add(observer);
    }

    @Override
    public void searchRecipe(String keyword) {
        if (!NetworkUtils.isNetworkAvailable(getApplication())) {
            fallback();
            return;
        }
        getClient().searchRecipe(keyword);
    }

    void fallback() {
        notifyAllObservers(Recipe.recentItems());
    }

    void notifyAllObservers(List<Recipe> recipes) {
        ConnectableObservable<List<Recipe>> connectableObservable = Observable.just(recipes).publish();
        for (Observer<List<Recipe>> subscriber : mSubscribers) {
            connectableObservable.subscribeOn(Schedulers.immediate()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

}
