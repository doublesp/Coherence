package com.doublesp.coherence.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interactors.MockRecipeInteractor;
import com.doublesp.coherence.viewholders.PlanViewHolder;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by pinyaoting on 11/16/16.
 */

public class PlanArrayAdapter extends RecyclerView.Adapter {

    MockRecipeInteractor mPlanInteractor;

    public PlanArrayAdapter() {
        // TODO: implement actual interactor
        mPlanInteractor = new MockRecipeInteractor(null, null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlanViewHolder) {
//            Plan viewModel = mPlanInteractor.getItemAtPos(position);
//            PlanViewHolder viewHolder = (PlanViewHolder) holder;
//            viewHolder.setViewModel(viewModel);
//            viewHolder.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
//        return mPlanInteractor.getItemCount();
        return 0;
    }
}
