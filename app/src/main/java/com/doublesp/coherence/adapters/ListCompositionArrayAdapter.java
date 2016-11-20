package com.doublesp.coherence.adapters;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaActionHandlerInterface;
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
    IdeaActionHandlerInterface mIdeaActionHandler;
    final Observer<Integer> mObserver;
    BlankIdeaViewHolder mBlankIdeaViewHolder;

    public ListCompositionArrayAdapter(IdeaInteractorInterface ideaInteractor, IdeaActionHandlerInterface ideaActionHandler) {
        mIdeaInteractor = ideaInteractor;
        mIdeaActionHandler = ideaActionHandler;
        mObserver = new Observer<Integer>() {
            int mState;
            @Override
            public void onCompleted() {
                switch (mState) {
                    case R.id.idea_state_idea_loaded:
                        notifyDataSetChanged();
                        break;
                    case R.id.idea_state_suggestion_loaded:
                        int start = mIdeaInteractor.getUserIdeaCount() + 1;
                        int end = mIdeaInteractor.getIdeaCount() - 1;
                        if (end > start) {
                            notifyItemRangeChanged(start, end);
                        }
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer state) {
                mState = state;
            }
        };
        mIdeaInteractor.subscribe(mObserver);
        mIdeaInteractor.getSuggestions(null);
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
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case R.id.idea_type_user_generated:
                view = inflater.inflate(R.layout.item_idea, parent, false);
                holder = new IdeaViewHolder(view);
                break;
            case R.id.idea_type_suggestion:
                view = inflater.inflate(R.layout.item_idea_suggestions, parent, false);
                holder = new SuggestedIdeaViewHolder(view);
                break;
            case R.id.idea_type_blank:
                view = inflater.inflate(R.layout.item_idea_blank, parent, false);
                holder = new BlankIdeaViewHolder(view);
                break;
        }
        if (holder instanceof IdeaViewHolderInterface) {
            IdeaViewHolderInterface ideaViewHolder = (IdeaViewHolderInterface) holder;
            ideaViewHolder.setHandler(mIdeaActionHandler);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Idea idea = mIdeaInteractor.getIdeaAtPos(position);
        if (holder instanceof IdeaViewHolderInterface) {
            IdeaViewHolderInterface ideaViewHolder = (IdeaViewHolderInterface) holder;
            ideaViewHolder.setPosition(position);
            ideaViewHolder.setViewModel(idea);
            ideaViewHolder.executePendingBindings();
        }
    }



    @Override
    public int getItemCount() {
        return mIdeaInteractor.getIdeaCount();
    }
}
