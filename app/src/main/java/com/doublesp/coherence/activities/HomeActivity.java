package com.doublesp.coherence.activities;

import com.crashlytics.android.Crashlytics;
import com.doublesp.coherence.R;
import com.doublesp.coherence.actions.IdeaCreationActionHandler;
import com.doublesp.coherence.actions.ListFragmentActionHandler;
import com.doublesp.coherence.adapters.HomeFragmentPagerAdapter;
import com.doublesp.coherence.application.CoherenceApplication;
import com.doublesp.coherence.databinding.ActivityHomeBinding;
import com.doublesp.coherence.dependencies.components.presentation.HomeActivitySubComponent;
import com.doublesp.coherence.dependencies.modules.presentation.HomeActivityModule;
import com.doublesp.coherence.fragments.IdeaPreviewFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.fragments.MultipleIdeaPreviewFragment;
import com.doublesp.coherence.interfaces.presentation.HomeInjectorInterface;
import com.doublesp.coherence.utils.CoherenceTabUtils;
import com.doublesp.coherence.viewmodels.Idea;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import io.fabric.sdk.android.Fabric;

public class HomeActivity extends AppCompatActivity implements HomeInjectorInterface, IdeaCreationActionHandler.IdeaPreviewHandlerInterface, ListFragmentActionHandler.IdeaShareHandlerInterface {

    static final String IDEA_PREVIEW_FRAGMENT = "IDEA_PREVIEW_FRAGMENT";
    ActivityHomeBinding binding;
    HomeActivitySubComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.viewpager.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager(), HomeActivity.this));
        binding.tabs.setupWithViewPager(binding.viewpager);
        CoherenceTabUtils.bindIcons(HomeActivity.this, binding.viewpager, binding.tabs);
    }

    public HomeActivitySubComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent =
                    ((CoherenceApplication) getApplication()).getPresentationLayerComponent()
                            .newListCompositionActivitySubComponent(
                                    new HomeActivityModule(this, R.id.idea_category_recipe));
//                                    new HomeActivityModule(this, R.id.idea_category_debug));  // NOTE: use idea_category_debug for mock data
        }
        return mActivityComponent;
    }

    @Override
    public void showPreviewDialog(Idea idea) {
        IdeaPreviewFragment previewDialog = IdeaPreviewFragment.newInstance(idea);
        previewDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        previewDialog.show(getSupportFragmentManager(), IDEA_PREVIEW_FRAGMENT);
    }

    public void share(Intent i) {
        startActivity(i);
    }

    @Override
    public void inject(ListCompositionFragment fragment) {
        getActivityComponent().inject(fragment);
    }

    @Override
    public void inject(MultipleIdeaPreviewFragment fragment) {
        getActivityComponent().inject(fragment);
    }
}
