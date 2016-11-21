package com.doublesp.coherence.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doublesp.coherence.databinding.ItemIdeaSuggestionsBinding;
import com.doublesp.coherence.interfaces.presentation.IdeaActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaViewHolderInterface;
import com.doublesp.coherence.viewmodels.Idea;

/**
 * Created by pinyaoting on 11/12/16.
 */

public class SuggestedIdeaViewHolder extends RecyclerView.ViewHolder implements
        IdeaViewHolderInterface {

    ItemIdeaSuggestionsBinding binding;

    public SuggestedIdeaViewHolder(View itemView) {
        super(itemView);
        binding = ItemIdeaSuggestionsBinding.bind(itemView);
    }

    @Override
    public void setPosition(int position) {
        binding.setPos(position);
    }

    @Override
    public void setViewModel(Idea viewModel) {
        binding.setViewModel(viewModel);
    }

    @Override
    public void setHandler(IdeaActionHandlerInterface handler) {
        binding.setHandler(handler);
    }

    @Override
    public void executePendingBindings() {
        binding.executePendingBindings();
    }

}
