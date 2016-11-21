package com.doublesp.coherence.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.viewholders.IdeaSelectorIdeaViewHolder;
import com.doublesp.coherence.viewmodels.Idea;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by pinyaoting on 11/14/16.
 */

public class IdeaSelectorArrayAdapter extends RecyclerView.Adapter {

    IdeaInteractorInterface mIdeaInteractor;

    public IdeaSelectorArrayAdapter(IdeaInteractorInterface ideaInteractor) {
        mIdeaInteractor = ideaInteractor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_idea_selector_idea, parent, false);
        return new IdeaSelectorIdeaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Idea idea = mIdeaInteractor.getIdeaAtPos(position);
        if (holder instanceof IdeaSelectorIdeaViewHolder) {
            IdeaSelectorIdeaViewHolder ideaViewHolder = (IdeaSelectorIdeaViewHolder) holder;
            ideaViewHolder.setViewModel(idea);
            ideaViewHolder.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return mIdeaInteractor.getUserIdeaCount();
    }
}
