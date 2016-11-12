package com.doublesp.coherence.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.doublesp.coherence.R;
import com.doublesp.coherence.application.CoherenceApplication;
import com.doublesp.coherence.databinding.ActivityExploreBinding;
import com.doublesp.coherence.dependencies.components.presentation.ExploreActivitySubComponent;
import com.doublesp.coherence.dependencies.modules.presentation.ExploreActivityModule;
import com.doublesp.coherence.fragments.ExploreFragment;
import com.doublesp.coherence.interfaces.presentation.ExploreFragmentInjector;

public class ExploreActivity extends AppCompatActivity implements ExploreFragmentInjector {

    static final String EXPLORE_FRAGMENT = "EXPLORE_FRAGMENT";
    ActivityExploreBinding binding;
    ExploreActivitySubComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_explore);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContainer, ExploreFragment.newInstance(), EXPLORE_FRAGMENT)
                .commit();
    }

    private ExploreActivitySubComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = ((CoherenceApplication) getApplication()).getPresentationLayerComponent().newExploreActivitySubComponent(new ExploreActivityModule(this));
        }
        return mActivityComponent;
    }

    @Override
    public void inject(ExploreFragment fragment) {
        getActivityComponent().inject(fragment);
    }
}
