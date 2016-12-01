package com.doublesp.coherence.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doublesp.coherence.databinding.ItemGoalBinding;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.viewmodels.Goal;

/**
 * Created by pinyaoting on 11/26/16.
 */

public class GoalViewHolder extends RecyclerView.ViewHolder {

    ItemGoalBinding binding;

    public GoalViewHolder(View itemView) {
        super(itemView);
        binding = ItemGoalBinding.bind(itemView);
    }

    public void setPosition(int position) {
        binding.setPos(position);
    }

    public void setViewModel(Goal viewModel) {
        binding.setViewModel(viewModel);
    }

    public void setHandler(GoalActionHandlerInterface handler) {
        binding.setHandler(handler);
    }

    public void executePendingBindings() {
        binding.executePendingBindings();
    }
}
