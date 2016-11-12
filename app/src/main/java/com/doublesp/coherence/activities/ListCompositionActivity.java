package com.doublesp.coherence.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.doublesp.coherence.R;
import com.doublesp.coherence.application.CoherenceApplication;
import com.doublesp.coherence.databinding.ActivityListCompositionBinding;
import com.doublesp.coherence.dependencies.components.presentation.ListCompositionActivitySubComponent;
import com.doublesp.coherence.dependencies.modules.presentation.ListCompositionActivityModule;
import com.doublesp.coherence.fragments.ExploreFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.interfaces.presentation.ListCompositionFragmentInjectorInterface;

public class ListCompositionActivity extends AppCompatActivity implements ListCompositionFragmentInjectorInterface {

    static final String LIST_COMPOSITION_FRAGMENT = "LIST_COMPOSITION_FRAGMENT";
    ActivityListCompositionBinding binding;
    ListCompositionActivitySubComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_composition);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flListCompositionContainer, ListCompositionFragment.newInstance(), LIST_COMPOSITION_FRAGMENT)
                .commit();
    }

    public ListCompositionActivitySubComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = ((CoherenceApplication) getApplication()).getPresentationLayerComponent().newListCompositionActivitySubComponent(new ListCompositionActivityModule(this));
        }
        return mActivityComponent;
    }

    @Override
    public void inject(ListCompositionFragment fragment) {

    }
}
