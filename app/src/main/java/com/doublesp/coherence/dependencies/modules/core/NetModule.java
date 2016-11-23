package com.doublesp.coherence.dependencies.modules.core;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.doublesp.coherence.R;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;


/**
 * Created by pinyaoting on 10/26/16.
 */

@Module
public class NetModule {

    public NetModule() {
    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .cache(cache)
                .build();
        return client;
    }

    @Provides
    @Singleton
    @IntoMap
    @IntKey(R.id.idea_category_recipe)
    Retrofit provideEdamamRetrofit(Gson gson, OkHttpClient okHttpClient, Application application) {
        String endpoint = application.getString(R.string.api_endpoint_recipe);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    @IntoMap
    @IntKey(R.id.idea_category_recipe_v2)
    Retrofit provideSpoonacularRetrofit(Gson gson, OkHttpClient okHttpClient,
            Application application) {
        String endpoint = application.getString(R.string.api_endpoint_recipe_v2);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(okHttpClient)
                .build();
        return retrofit;
    }

}
