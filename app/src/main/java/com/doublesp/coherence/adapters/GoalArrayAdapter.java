package com.doublesp.coherence.adapters;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.viewholders.GoalViewHolder;
import com.doublesp.coherence.viewmodels.Goal;

import rx.Observer;

public class GoalArrayAdapter
        extends RecyclerView.Adapter {

    GoalInteractorInterface mInteractor;
    GoalActionHandlerInterface mActionHandler;

    public GoalArrayAdapter(GoalInteractorInterface interactor,
            GoalActionHandlerInterface actionHandler) {
        mInteractor = interactor;
        mActionHandler = actionHandler;
        mInteractor.subscribeToGoalStateChange(new Observer<ViewState>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ViewState state) {

                int start;
                int count;
                switch (state.getState()) {
                    case R.id.state_refreshing:
                        // TODO: reflect pending state on UI
                        break;
                    case R.id.state_loaded:
                        switch (state.getOperation()) {
                            case RELOAD:
                                notifyDataSetChanged();
                                break;
                            case ADD:
                                start = state.getStart();
                                notifyItemInserted(start);
                                break;
                            case INSERT:
                                start = state.getStart();
                                count = state.getCount();
                                notifyItemRangeInserted(start, count);
                                break;
                            case UPDATE:
                                start = state.getStart();
                                count = state.getCount();
                                if (start == -1) {
                                    return;
                                }
                                notifyItemRangeChanged(start, count);
                                break;
                            case REMOVE:
                                start = state.getStart();
                                count = state.getCount();
                                notifyItemRangeRemoved(start, count);
                                break;
                            case CLEAR:
                                notifyDataSetChanged();
                                break;
                        }
                        break;
                }
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
        Goal viewModel = mInteractor.getGoalAtPos(position);
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
        return mInteractor.getGoalCount();
    }
}
