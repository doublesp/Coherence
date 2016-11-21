package com.doublesp.coherence.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doublesp.coherence.databinding.ItemIdeaSelectorIdeaBinding;
import com.doublesp.coherence.viewmodels.Idea;

/**
 * Created by pinyaoting on 11/14/16.
 */

public class IdeaSelectorIdeaViewHolder extends RecyclerView.ViewHolder {

    ItemIdeaSelectorIdeaBinding binding;

    public IdeaSelectorIdeaViewHolder(View itemView) {
        super(itemView);
        binding = ItemIdeaSelectorIdeaBinding.bind(itemView);
    }

    public void setViewModel(Idea viewModel) {
        binding.setViewModel(viewModel);
    }

    public void executePendingBindings() {
        binding.executePendingBindings();
    }
}
