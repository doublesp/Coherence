package com.doublesp.coherence.repositories;

import com.doublesp.coherence.api.EdamamClient;
import com.doublesp.coherence.database.RecipeDatabase;
import com.doublesp.coherence.interfaces.data.RecipeRepositoryInterface;
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

import rx.Observer;
import rx.subjects.PublishSubject;

/**
 * Created by pinyaoting on 11/17/16.
 */

public class EdamamRepository implements RecipeRepositoryInterface {

    private Application mApplication;
    private EdamamClient mClient;

    public EdamamRepository(Application application, EdamamClient client) {
        mApplication = application;
        mClient = client;
        mPublisher = PublishSubject.create();
        getClient().subscribe(new Observer<RecipeResponse>() {
            @Override
            public void onCompleted() {
                mPublisher.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                mPublisher.onError(e);
            }

            @Override
            public void onNext(RecipeResponse recipeResponse) {
                List<Recipe> recipes = new ArrayList<Recipe>();
                for (RecipeResponseHit hit : recipeResponse.getResponses()) {
                    recipes.add(hit.getRecipe());
                }
                mPublisher.onNext(recipes);
                // persists into database in background thread
                FlowManager.getDatabase(RecipeDatabase.class)
                        .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<Recipe>() {
                                    @Override
                                    public void processModel(Recipe recipe) {
                                        recipe.save();
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
        });
    }

    protected EdamamClient getClient() {
        return mClient;
    }

    protected Application getApplication() {
        return mApplication;
    }

    PublishSubject<List<Recipe>> mPublisher;

    @Override
    public void subscribe(Observer<List<Recipe>> observer) {
        mPublisher.subscribe(observer);
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
        mPublisher.onNext(Recipe.recentItems());
    }
}
