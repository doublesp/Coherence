package com.doublesp.coherence.api;

import com.doublesp.coherence.interfaces.api.SpoonacularApiEndpointInterface;
import com.doublesp.coherence.models.v2.IngredientV2;
import com.doublesp.coherence.models.v2.RandomRecipeResponseV2;
import com.doublesp.coherence.models.v2.RecipeResponseV2;
import com.doublesp.coherence.models.v2.RecipeV2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by pinyaoting on 11/22/16.
 */

public class SpoonacularClient {

    static final int DELAY_BETWEEN_API_CALLS = 1000;
    SpoonacularApiEndpointInterface apiService;
    List<Observer<RecipeResponseV2>> mSubscribers;
    List<Observer<RecipeV2>> mDetailSubscribers;
    List<Observer<List<RecipeV2>>> mRecipeSubscribers;
    List<Observer<RandomRecipeResponseV2>> mRandomRecipeSubscribers;
    List<Observer<List<RecipeV2>>> mAutoCompleteRecipeSubscribers;
    List<Observer<List<IngredientV2>>> mAutoCompleteIngredientSubscribers;

    public SpoonacularClient(SpoonacularApiEndpointInterface apiService) {
        this.apiService = apiService;
        mSubscribers = new ArrayList<>();
        mRecipeSubscribers = new ArrayList<>();
        mRandomRecipeSubscribers = new ArrayList<>();
        mDetailSubscribers = new ArrayList<>();
        mAutoCompleteRecipeSubscribers = new ArrayList<>();
        mAutoCompleteIngredientSubscribers = new ArrayList<>();
    }

    public void searchRecipe(String keyword, int count, int offset) {
        Observable<RecipeResponseV2> call = apiService.searchRecipe(keyword, count, offset);
        ConnectableObservable connectableObservable = call.publish();
        connectableObservable.delay(DELAY_BETWEEN_API_CALLS, TimeUnit.MILLISECONDS);
        for (Observer<RecipeResponseV2> subscriber : mSubscribers) {
            connectableObservable.subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

    public void searchRecipeDetail(String id) {
        Observable<RecipeV2> call = apiService.searchRecipeDetail(id);
        ConnectableObservable connectableObservable = call.publish();
        connectableObservable.delay(DELAY_BETWEEN_API_CALLS, TimeUnit.MILLISECONDS);
        for (Observer<RecipeV2> subscriber : mDetailSubscribers) {
            connectableObservable.subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

    public void searchRecipeByIngredients(
            String ingredients,
            boolean fillIngredients,
            int number,
            int ranking) {
        Observable<List<RecipeV2>> call = apiService.searchRecipeByIngredients(
                ingredients,
                fillIngredients,
                number,
                ranking);
        ConnectableObservable connectableObservable = call.publish();
        connectableObservable.delay(DELAY_BETWEEN_API_CALLS, TimeUnit.MILLISECONDS);
        for (Observer<List<RecipeV2>> subscriber : mRecipeSubscribers) {
            connectableObservable.subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

    public void randomRecipe(int count) {
        Observable<RandomRecipeResponseV2> call = apiService.randomRecipe(count);
        ConnectableObservable connectableObservable = call.publish();
        connectableObservable.delay(DELAY_BETWEEN_API_CALLS, TimeUnit.MILLISECONDS);
        for (Observer<RandomRecipeResponseV2> subscriber : mRandomRecipeSubscribers) {
            connectableObservable.subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

    public void autoCompleteIngredient(String keyword, int number) {
        Observable<List<IngredientV2>> call = apiService.autocompleteIngredient(keyword, number);
        ConnectableObservable connectableObservable = call.publish();
        connectableObservable.delay(DELAY_BETWEEN_API_CALLS, TimeUnit.MILLISECONDS);
        for (Observer<List<IngredientV2>> subscriber : mAutoCompleteIngredientSubscribers) {
            connectableObservable.subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

    public void autoCompleteRecipe(String keyword, int number) {
        Observable<List<RecipeV2>> call = apiService.autocompleteRecipe(keyword, number);
        ConnectableObservable connectableObservable = call.publish();
        connectableObservable.delay(DELAY_BETWEEN_API_CALLS, TimeUnit.MILLISECONDS);
        for (Observer<List<RecipeV2>> subscriber : mAutoCompleteRecipeSubscribers) {
            connectableObservable.subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectableObservable.connect();
    }

    public void subscribeRecipe(Observer<RecipeResponseV2> observer) {
        mSubscribers.add(observer);
    }

    public void subscribeRecipeDetail(Observer<RecipeV2> observer) {
        mDetailSubscribers.add(observer);
    }

    public void subscribeRecipeByIngredients(Observer<List<RecipeV2>> observer) {
        mRecipeSubscribers.add(observer);
    }

    public void subscribeRandomRecipe(Observer<RandomRecipeResponseV2> observer) {
        mRandomRecipeSubscribers.add(observer);
    }

    public void subscribeAutoCompleteIngredient(Observer<List<IngredientV2>> observer) {
        mAutoCompleteIngredientSubscribers.add(observer);
    }

    public void subscribeAutoCompleteRecipe(Observer<List<RecipeV2>> observer) {
        mAutoCompleteRecipeSubscribers.add(observer);
    }
}
