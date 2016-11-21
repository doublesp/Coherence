package com.doublesp.coherence.dependencies.components.application;

import android.app.Application;

import com.doublesp.coherence.dependencies.modules.core.AppModule;
import com.doublesp.coherence.dependencies.modules.core.NetModule;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by pinyaoting on 11/11/16.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface ApplicationComponent {

    Application application();

    Map<Integer, Retrofit> retrofitMap();

}
