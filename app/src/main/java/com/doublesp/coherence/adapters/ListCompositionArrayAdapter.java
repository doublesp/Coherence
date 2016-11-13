package com.doublesp.coherence.adapters;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaViewHolderInterface;
import com.doublesp.coherence.viewholders.BlankIdeaViewHolder;
import com.doublesp.coherence.viewholders.IdeaViewHolder;
import com.doublesp.coherence.viewholders.SuggestedIdeaViewHolder;
import com.doublesp.coherence.viewmodels.Idea;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observer;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by pinyaoting on 11/12/16.
 */

public class ListCompositionArrayAdapter extends RecyclerView.Adapter {

    IdeaInteractorInterface mIdeaInteractor;

    public ListCompositionArrayAdapter(IdeaInteractorInterface ideaInteractor) {
        mIdeaInteractor = ideaInteractor;
        mIdeaInteractor.subscribe(new Observer<Idea>() {
            @Override
            public void onCompleted() {
                notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Idea idea) {

            }
        });
        mIdeaInteractor.getRelatedIdeas(null);
    }

    @Override
    public int getItemViewType(int position) {
        Idea idea = mIdeaInteractor.getIdeaAtPos(position);
        return idea.getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view;
        switch (viewType) {
            case R.id.idea_type_user_generated:
                view = inflater.inflate(R.layout.item_idea, parent, false);
                return new IdeaViewHolder(view);
            case R.id.idea_type_suggestion:
                view = inflater.inflate(R.layout.item_idea_suggestions, parent, false);
                return new SuggestedIdeaViewHolder(view);
            case R.id.idea_type_blank:
                view = inflater.inflate(R.layout.item_idea_blank, parent, false);
                return new BlankIdeaViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Idea idea = mIdeaInteractor.getIdeaAtPos(position);
        if (holder instanceof IdeaViewHolderInterface) {
            IdeaViewHolderInterface ideaViewHolder = (IdeaViewHolderInterface) holder;
            ideaViewHolder.setViewModel(idea);
            ideaViewHolder.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return mIdeaInteractor.getIdeaCount();
    }
}
