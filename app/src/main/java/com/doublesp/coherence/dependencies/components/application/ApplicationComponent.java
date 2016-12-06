package com.doublesp.coherence.dependencies.components.application;

import android.app.Application;

import com.doublesp.coherence.dependencies.modules.core.AppModule;
import com.doublesp.coherence.dependencies.modules.core.NetModule;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface ApplicationComponent {

    Application application();

    Map<Integer, Retrofit> retrofitMap();

}
