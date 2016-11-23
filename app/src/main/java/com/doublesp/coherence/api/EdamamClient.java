package com.doublesp.coherence.api;

import com.doublesp.coherence.interfaces.api.EdamamApiEndpointInterface;
import com.doublesp.coherence.models.v1.RecipeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by pinyaoting on 11/17/16.
 */

public class EdamamClient {

    static final int DELAY_BETWEEN_API_CALLS = 1000;
    static final String APP_ID = "57d8d640";
    static final String API_KEY = "7428255c3ebf56e64ec0caab08c9b174";
    EdamamApiEndpointInterface apiService;
    List<Observer<RecipeResponse>> mSubscribers;

    public EdamamClient(EdamamApiEndpointInterface apiService) {
        this.apiService = apiService;
        mSubscribers = new ArrayList<>();
    }

    public void searchRecipe(String keyword) {
        Observable<RecipeResponse> call = apiService.searchRecipe(keyword, APP_ID, API_KEY);
        asyncCall(call.publish());
    }

    public void subscribe(Observer subscriber) {
        mSubscribers.add(subscriber);
    }

    private void asyncCall(ConnectableObservable connectedObservable) {
        // TODO: remove this delay once we are confident we aren't making unneccesary api calls
        // to this endpoint
        connectedObservable.delay(DELAY_BETWEEN_API_CALLS, TimeUnit.MILLISECONDS);
        for (Observer<RecipeResponse> subscriber : mSubscribers) {
            connectedObservable.subscribeOn(Schedulers.io()).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(subscriber);
        }
        connectedObservable.connect();
    }

}
