package com.doublesp.coherence.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doublesp.coherence.databinding.ItemIdeaBinding;
import com.doublesp.coherence.interfaces.presentation.IdeaViewHolderInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.viewmodels.Idea;

public class IdeaViewHolder extends RecyclerView.ViewHolder implements IdeaViewHolderInterface {

    ItemIdeaBinding binding;

    public IdeaViewHolder(View itemView) {
        super(itemView);
        binding = ItemIdeaBinding.bind(itemView);
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
    public void setHandler(final ListFragmentActionHandlerInterface handler) {
        binding.setHandler(handler);
    }

    @Override
    public void executePendingBindings() {
        binding.executePendingBindings();
    }
}
