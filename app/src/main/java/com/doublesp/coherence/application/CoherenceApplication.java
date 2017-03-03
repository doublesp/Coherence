package com.doublesp.coherence.application;

import android.support.multidex.MultiDexApplication;
import android.support.v4.content.ContextCompat;

import com.batch.android.Batch;
import com.batch.android.Config;
import com.doublesp.coherence.BuildConfig;
import com.doublesp.coherence.R;
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
import com.google.firebase.database.FirebaseDatabase;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

public class CoherenceApplication extends MultiDexApplication {

    private ApplicationComponent mApplicationComponent;
    private DataLayerComponent mDataLayerComponent;
    private DomainLayerComponent mDomainLayerComponent;
    private PresentationLayerComponent mPresentationLayerComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
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
                .domainLayerModule(new DomainLayerModule(getBaseContext()))
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

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Batch.Push.setGCMSenderId(BuildConfig.GCM_SENDER_ID);

        Batch.setConfig(new Config(BuildConfig.BATCH_APIKEY)); // live
        Batch.Push.setNotificationsColor(ContextCompat.getColor(this, R.color.colorPrimary));
        //Batch.Push.setSmallIconResourceId(R.drawable.ic_notification);
    }

    public PresentationLayerComponent getPresentationLayerComponent() {
        return mPresentationLayerComponent;
    }
}
