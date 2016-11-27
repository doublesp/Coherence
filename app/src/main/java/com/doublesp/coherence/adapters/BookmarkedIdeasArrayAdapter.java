package com.doublesp.coherence.adapters;

import com.doublesp.coherence.R;
import com.doublesp.coherence.viewholders.PlanViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by pinyaoting on 11/16/16.
 */

public class BookmarkedIdeasArrayAdapter extends RecyclerView.Adapter {

    public BookmarkedIdeasArrayAdapter() {
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
