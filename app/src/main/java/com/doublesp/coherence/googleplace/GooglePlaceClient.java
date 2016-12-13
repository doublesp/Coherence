package com.doublesp.coherence.googleplace;

import com.doublesp.coherence.googleplace.gplace.GPlace;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by sduan on 12/4/16.
 */

public class GooglePlaceClient {
    static final String API_KEY = "AIzaSyCBjupD4UBQ4puue5J2uqjKpjpvpTJeB50";
    static final String DEFAULT_LOCATION = "37.4227000,-122.1378420";
    static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    GooglePlaceApiEndpointInterface endpoint;

    private RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
    private static GooglePlaceClient googlePlaceClient;

    public synchronized static GooglePlaceClient newInstance() {
        if (googlePlaceClient == null) {
            googlePlaceClient = new GooglePlaceClient();
        }
        return googlePlaceClient;
    }

    private GooglePlaceClient() {
        endpoint = createService();
    }

    private GooglePlaceApiEndpointInterface createService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();
        GooglePlaceApiEndpointInterface endpoint = retrofit.create(GooglePlaceApiEndpointInterface.class);
        return endpoint;
    }

    public Observable<GPlace> searchStores(String latAndLong) {
        if (latAndLong == null || latAndLong.length() == 0) {
            latAndLong = DEFAULT_LOCATION;
        }
        Observable<GPlace> observable = endpoint.search(latAndLong, "3000", "grocery_or_supermarket", API_KEY);
        return observable;
    }


    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();
}
