package com.doublesp.coherence.viewholders;

import com.doublesp.coherence.databinding.ItemIdeaSearchBinding;
import com.doublesp.coherence.interfaces.presentation.IdeaActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaViewHolderInterface;
import com.doublesp.coherence.viewmodels.Idea;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by pinyaoting on 11/26/16.
 */

public class IdeaSearchViewHolder extends RecyclerView.ViewHolder implements IdeaViewHolderInterface {

    ItemIdeaSearchBinding binding;

    public IdeaSearchViewHolder(View itemView) {
        super(itemView);
        binding = ItemIdeaSearchBinding.bind(itemView);
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
