package com.doublesp.coherence.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.ActivityExploreBinding;

public class ExploreActivity extends AppCompatActivity {

    ActivityExploreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_explore);

//        binding.flContent
        FragmentManager fm = getSupportFragmentManager();

//        setContentView(R.layout.activity_explore);
    }
}
