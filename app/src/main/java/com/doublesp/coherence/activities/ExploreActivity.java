package com.doublesp.coherence.activities;

import com.crashlytics.android.Crashlytics;
import com.doublesp.coherence.R;
import com.doublesp.coherence.adapters.ExploreFragmentPagerAdapter;
import com.doublesp.coherence.application.CoherenceApplication;
import com.doublesp.coherence.databinding.ActivityExploreBinding;
import com.doublesp.coherence.dependencies.components.presentation.ExploreActivitySubComponent;
import com.doublesp.coherence.dependencies.modules.presentation.ExploreActivityModule;
import com.doublesp.coherence.fragments.ExploreFragment;
import com.doublesp.coherence.interfaces.presentation.ExploreFragmentInjectorInterface;
import com.doublesp.coherence.utils.CoherenceTabUtils;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.fabric.sdk.android.Fabric;

public class ExploreActivity extends AppCompatActivity implements ExploreFragmentInjectorInterface {

    ActivityExploreBinding binding;
    ExploreActivitySubComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_explore);
        binding.viewpager.setAdapter(new ExploreFragmentPagerAdapter(getSupportFragmentManager(), ExploreActivity.this));
        binding.tabs.setupWithViewPager(binding.viewpager);
        CoherenceTabUtils.bindIcons(ExploreActivity.this, binding.viewpager, binding.tabs);
    }

    private ExploreActivitySubComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent =
                    ((CoherenceApplication) getApplication()).getPresentationLayerComponent()
                            .newExploreActivitySubComponent(
                            new ExploreActivityModule(this));
        }
        return mActivityComponent;
    }

    public void startListCompositionActivity(int category) {
        Intent i = new Intent(ExploreActivity.this, ListCompositionActivity.class);
        i.putExtra(getString(R.string.category), category);
        startActivity(i);
    }

    @Override
    public void inject(ExploreFragment fragment) {
        getActivityComponent().inject(fragment);
    }
}
