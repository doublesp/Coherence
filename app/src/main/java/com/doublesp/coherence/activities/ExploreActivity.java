package com.doublesp.coherence.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.ActivityExploreBinding;
import com.doublesp.coherence.fragments.ExploreFragment;

public class ExploreActivity extends AppCompatActivity {

    static final String EXPLORE_FRAGMENT = "EXPLORE_FRAGMENT";
    ActivityExploreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_explore);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContainer, ExploreFragment.newInstance())
                .commit();
    }
}
