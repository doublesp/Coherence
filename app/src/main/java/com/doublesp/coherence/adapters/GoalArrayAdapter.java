package com.doublesp.coherence.adapters;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
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

public class GoalArrayAdapter
        extends RecyclerView.Adapter {

    GoalInteractorInterface mInteractor;
    GoalActionHandlerInterface mActionHandler;

    public GoalArrayAdapter(GoalInteractorInterface interactor, GoalActionHandlerInterface actionHandler) {
        mInteractor = interactor;
        mActionHandler = actionHandler;
        mInteractor.subscribeToGoalStateChange(new Observer<Integer>() {
            int mState;

            @Override
            public void onCompleted() {
                switch (mState) {
                    case R.id.state_loaded:
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
        mInteractor.search(null);
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
