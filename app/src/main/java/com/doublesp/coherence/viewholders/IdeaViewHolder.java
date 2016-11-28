package com.doublesp.coherence.viewholders;

import com.doublesp.coherence.databinding.ItemIdeaBinding;
import com.doublesp.coherence.interfaces.presentation.IdeaViewHolderInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.viewmodels.Idea;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by pinyaoting on 11/12/16.
 */

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

    }

    @Override
    public void executePendingBindings() {
        binding.executePendingBindings();
    }
}
