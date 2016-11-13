package com.doublesp.coherence.dependencies.modules.presentation;

import com.doublesp.coherence.R;
import com.doublesp.coherence.activities.ListCompositionActivity;
import com.doublesp.coherence.adapters.ListCompositionArrayAdapter;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import android.support.v7.widget.RecyclerView;

import java.util.Map;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pinyaoting on 11/11/16.
 */

@Module
public class ListCompositionActivityModule {

    private final ListCompositionActivity mActivity;
    private final int mCategory;
    public ListCompositionActivityModule(ListCompositionActivity activity, int category) {
        mActivity = activity;
        mCategory = category;
    }

    @Provides
    @PresentationLayerScope
    public RecyclerView.Adapter<RecyclerView.ViewHolder> providesListCompositionArrayAdapter(Map<Integer, IdeaInteractorInterface> ideaInteractors) {
//        IdeaInteractorInterface ideaInteractor = ideaInteractors.get(mCategory);
        IdeaInteractorInterface ideaInteractor = ideaInteractors.get(R.id.idea_category_movies); // TODO: implement interactors for different categories
        return new ListCompositionArrayAdapter(ideaInteractor);
    }
}
