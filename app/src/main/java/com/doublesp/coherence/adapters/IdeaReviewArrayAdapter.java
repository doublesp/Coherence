package com.doublesp.coherence.adapters;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.presentation.IdeaViewHolderInterface;
import com.doublesp.coherence.viewholders.IdeaViewHolder;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import java.util.List;

/**
 * Created by pinyaoting on 11/21/16.
 */

public class IdeaReviewArrayAdapter extends RecyclerView.Adapter {

    List<Idea> mIdeas;

    public IdeaReviewArrayAdapter(Plan plan) {
        mIdeas = plan.getIdeas();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_idea, parent, false);
        RecyclerView.ViewHolder holder = new IdeaViewHolder(view);
        // TODO: handle actions
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Idea idea = mIdeas.get(position);
        if (holder instanceof IdeaViewHolderInterface) {
            IdeaViewHolderInterface ideaViewHolder = (IdeaViewHolderInterface) holder;
            ideaViewHolder.setPosition(position);
            ideaViewHolder.setViewModel(idea);
            ideaViewHolder.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return mIdeas.size();
    }
}
