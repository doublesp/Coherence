package com.doublesp.coherence.adapters;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.presentation.IdeaSearchInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaViewHolderInterface;
import com.doublesp.coherence.viewholders.IdeaSearchViewHolder;
import com.doublesp.coherence.viewmodels.Idea;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observer;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by pinyaoting on 11/26/16.
 */

public class IdeaSearchResultArrayAdapter extends RecyclerView.Adapter {

    IdeaSearchInteractorInterface mSearchInteractor;

    public IdeaSearchResultArrayAdapter(IdeaSearchInteractorInterface searchInteractor) {
        mSearchInteractor = searchInteractor;
        mSearchInteractor.subscribeToStateChange(new Observer<Integer>() {
            int mState;

            @Override
            public void onCompleted() {
                switch (mState) {
                    case R.id.suggestion_state_loaded:
                        notifyDataSetChanged();
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
        });
        mSearchInteractor.search(null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_idea_search, parent, false);
        return new IdeaSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Idea viewModel = mSearchInteractor.getResultAtPos(position);
        if (holder instanceof IdeaViewHolderInterface) {
            IdeaViewHolderInterface ideaViewHolder = (IdeaViewHolderInterface) holder;
            ideaViewHolder.setPosition(position);
            ideaViewHolder.setViewModel(viewModel);
            ideaViewHolder.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return mSearchInteractor.getResultCount();
    }
}
