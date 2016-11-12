package com.doublesp.coherence.application;

import android.app.Application;

import com.doublesp.coherence.dependencies.components.application.ApplicationComponent;
import com.doublesp.coherence.dependencies.components.application.DaggerApplicationComponent;
import com.doublesp.coherence.dependencies.components.data.DaggerDataLayerComponent;
import com.doublesp.coherence.dependencies.components.data.DataLayerComponent;
import com.doublesp.coherence.dependencies.components.domain.DaggerDomainLayerComponent;
import com.doublesp.coherence.dependencies.components.domain.DomainLayerComponent;
import com.doublesp.coherence.dependencies.components.presentation.DaggerPresentationLayerComponent;
import com.doublesp.coherence.dependencies.components.presentation.PresentationLayerComponent;
import com.doublesp.coherence.dependencies.modules.core.AppModule;
import com.doublesp.coherence.dependencies.modules.core.NetModule;
import com.doublesp.coherence.dependencies.modules.data.DataLayerModule;
import com.doublesp.coherence.dependencies.modules.domain.DomainLayerModule;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by pinyaoting on 11/11/16.
 */

public class CoherenceApplication extends Application {

    private ApplicationComponent mApplicationComponent;
    private DataLayerComponent mDataLayerComponent;
    private DomainLayerComponent mDomainLayerComponent;
    private PresentationLayerComponent mPresentationLayerComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();

        mDataLayerComponent = DaggerDataLayerComponent.builder()
                .applicationComponent(mApplicationComponent)
                .dataLayerModule(new DataLayerModule())
                .build();

        mDomainLayerComponent = DaggerDomainLayerComponent.builder()
                .dataLayerComponent(mDataLayerComponent)
                .domainLayerModule(new DomainLayerModule())
                .build();

        mPresentationLayerComponent = DaggerPresentationLayerComponent.builder()
                .domainLayerComponent(mDomainLayerComponent)
                .build();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        FlowManager.init(new FlowConfig.Builder(this).build());
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
    }

    public PresentationLayerComponent getPresentationLayerComponent() {
        return mPresentationLayerComponent;
    }
}
