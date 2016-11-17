package com.doublesp.coherence.viewholders;

import com.doublesp.coherence.databinding.ItemPlanBinding;
import com.doublesp.coherence.viewmodels.Plan;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by pinyaoting on 11/16/16.
 */

public class PlanViewHolder extends RecyclerView.ViewHolder {

    ItemPlanBinding binding;

    public PlanViewHolder(View itemView) {
        super(itemView);
        binding = ItemPlanBinding.bind(itemView);
    }

    public void setViewModel(Plan viewModel) {
        binding.setViewModel(viewModel);
    }

    public void executePendingBindings() {
        binding.executePendingBindings();
    }
}
