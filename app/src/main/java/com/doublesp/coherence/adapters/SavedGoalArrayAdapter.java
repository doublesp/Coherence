package com.doublesp.coherence.adapters;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.viewholders.GoalViewHolder;
import com.doublesp.coherence.viewmodels.Goal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observer;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by pinyaoting on 11/27/16.
 */

public class SavedGoalArrayAdapter extends RecyclerView.Adapter {

    GoalInteractorInterface mInteractor;
    GoalActionHandlerInterface mActionHandler;

    public SavedGoalArrayAdapter(GoalInteractorInterface interactor,
                                 GoalActionHandlerInterface actionHandler) {
        mInteractor = interactor;
        mActionHandler = actionHandler;
        mInteractor.subscribeToSavedGoalStateChange(new Observer<ViewState>() {
            ViewState mState;

            @Override
            public void onCompleted() {
                int start;
                int count;
                switch (mState.getState()) {
                    case R.id.state_refreshing:
                        // TODO: reflect pending state on UI
                        break;
                    case R.id.state_loaded:
                        switch (mState.getOperation()) {
                            case RELOAD:
                                notifyDataSetChanged();
                                break;
                            case ADD:
                                start = mState.getStart();
                                notifyItemInserted(start);
                                break;
                            case INSERT:
                                start = mState.getStart();
                                count = mState.getCount();
                                notifyItemRangeInserted(start, count);
                                break;
                            case UPDATE:
                                start = mState.getStart();
                                count = mState.getCount();
                                notifyItemRangeChanged(start, count);
                                break;
                            case REMOVE:
                                start = mState.getStart();
                                count = mState.getCount();
                                notifyItemRangeRemoved(start, count);
                                break;
                            case CLEAR:
                                notifyDataSetChanged();
                                break;
                        }
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ViewState state) {
                mState = state;
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Goal viewModel = mInteractor.getSavedGoalAtPos(position);
        if (holder instanceof GoalViewHolder) {
            GoalViewHolder viewHolder = (GoalViewHolder) holder;
            viewHolder.setPosition(position);
            viewHolder.setViewModel(viewModel);
            viewHolder.setHandler(mActionHandler);
            viewHolder.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return mInteractor.getSavedGoalCount();
    }
}
