package com.doublesp.coherence.viewholders;

import com.doublesp.coherence.databinding.ItemIdeaBlankBinding;
import com.doublesp.coherence.interfaces.presentation.IdeaActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaViewHolderInterface;
import com.doublesp.coherence.viewmodels.Idea;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * Created by pinyaoting on 11/12/16.
 */

public class BlankIdeaViewHolder extends RecyclerView.ViewHolder implements IdeaViewHolderInterface {

    ItemIdeaBlankBinding binding;

    public BlankIdeaViewHolder(View itemView) {
        super(itemView);
        binding = ItemIdeaBlankBinding.bind(itemView);
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
    public void setHandler(final IdeaActionHandlerInterface handler) {
        final int pos = binding.getPos();
        binding.etIdea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.afterTextChanged(s, pos);
            }
        });
    }

    @Override
    public void executePendingBindings() {
        binding.executePendingBindings();
    }

}
