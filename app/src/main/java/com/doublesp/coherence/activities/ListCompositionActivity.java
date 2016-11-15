package com.doublesp.coherence.activities;

import com.doublesp.coherence.R;
import com.doublesp.coherence.application.CoherenceApplication;
import com.doublesp.coherence.databinding.ActivityListCompositionBinding;
import com.doublesp.coherence.dependencies.components.presentation.ListCompositionActivitySubComponent;
import com.doublesp.coherence.dependencies.modules.presentation.ListCompositionActivityModule;
import com.doublesp.coherence.fragments.IdeaPreviewFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.interfaces.presentation.ListCompositionInjectorInterface;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

public class ListCompositionActivity extends AppCompatActivity implements ListCompositionInjectorInterface {

    static final String LIST_COMPOSITION_FRAGMENT = "LIST_COMPOSITION_FRAGMENT";
    static final String IDEA_PREVIEW_FRAGMENT = "IDEA_PREVIEW_FRAGMENT";
    int mCategory;
    ActivityListCompositionBinding binding;
    ListCompositionActivitySubComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_composition);
        mCategory = getIntent().getIntExtra(getString(R.string.category), 0);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flListCompositionContainer, ListCompositionFragment.newInstance(), LIST_COMPOSITION_FRAGMENT)
                .commit();
    }

    public ListCompositionActivitySubComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent =
                    ((CoherenceApplication) getApplication()).getPresentationLayerComponent()
                            .newListCompositionActivitySubComponent(
                            new ListCompositionActivityModule(this, mCategory));
        }
        return mActivityComponent;
    }

    public void showPreviewDialog() {
        IdeaPreviewFragment previewDialog = IdeaPreviewFragment.newInstance();
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
    public void inject(IdeaPreviewFragment fragment) {
        getActivityComponent().inject(fragment);
    }
}
