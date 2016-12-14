package com.doublesp.coherence.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doublesp.coherence.databinding.ItemIdeaBinding;
import com.doublesp.coherence.utils.BindingAdapterUtils;
import com.doublesp.coherence.viewmodels.Idea;

/**
 * Created by pinyaoting on 12/6/16.
 */

public class SimpleIdeaViewHolder extends RecyclerView.ViewHolder {

    ItemIdeaBinding binding;

    public SimpleIdeaViewHolder(View itemView) {
        super(itemView);
        binding = ItemIdeaBinding.bind(itemView);
    }

    public void setPosition(int position) {
        binding.setPos(position);
    }

    public void setViewModel(Idea viewModel) {
        binding.setViewModel(viewModel);
        if (viewModel == null ||
                viewModel.getMeta() == null ||
                viewModel.getMeta().getImageUrl() == null) {
            return;
        }
        BindingAdapterUtils.loadImage(binding.ivIdea, viewModel.getMeta().getImageUrl());
    }

    public void executePendingBindings() {
        binding.executePendingBindings();
    }
}
