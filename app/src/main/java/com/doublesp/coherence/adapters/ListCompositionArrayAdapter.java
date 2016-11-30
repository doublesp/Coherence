package com.doublesp.coherence.adapters;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaViewHolderInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewholders.IdeaViewHolder;
import com.doublesp.coherence.viewholders.SuggestedIdeaViewHolder;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rx.Observer;

/**
 * Created by pinyaoting on 11/12/16.
 */

public class ListCompositionArrayAdapter extends RecyclerView.Adapter {

    IdeaInteractorInterface mIdeaInteractor;
    ListFragmentActionHandlerInterface mIdeaActionHandler;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListDatabaseReference;
    private DatabaseReference mShoppingListDatabaseReference;


    public ListCompositionArrayAdapter(IdeaInteractorInterface ideaInteractor,
            ListFragmentActionHandlerInterface ideaActionHandler) {
        mIdeaInteractor = ideaInteractor;
        mIdeaActionHandler = ideaActionHandler;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.USER_LISTS)
                .child(ConstantsAndUtils.getOwner(getContext()));
        mShoppingListDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.SHOPPING_LISTS);

        mIdeaInteractor.subscribeIdeaStateChange(new Observer<Integer>() {
            int mState;

            @Override
            public void onCompleted() {
                switch (mState) {
                    case R.id.state_loaded:
                        notifyDataSetChanged();
                        saveToFireBase();
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer state) {
                mState = state;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        Idea idea = mIdeaInteractor.getIdeaAtPos(position);
        return idea.getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case R.id.idea_type_user_generated:
                view = inflater.inflate(R.layout.item_idea, parent, false);
                holder = new IdeaViewHolder(view);
                break;
            case R.id.idea_type_suggestion:
                view = inflater.inflate(R.layout.item_idea_suggestions, parent, false);
                holder = new SuggestedIdeaViewHolder(view);
                break;
        }
        if (holder instanceof IdeaViewHolderInterface) {
            IdeaViewHolderInterface ideaViewHolder = (IdeaViewHolderInterface) holder;
            ideaViewHolder.setHandler(mIdeaActionHandler);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Idea idea = mIdeaInteractor.getIdeaAtPos(position);
        if (holder instanceof IdeaViewHolderInterface) {
            IdeaViewHolderInterface ideaViewHolder = (IdeaViewHolderInterface) holder;
            ideaViewHolder.setPosition(position);
            ideaViewHolder.setViewModel(idea);
            ideaViewHolder.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return mIdeaInteractor.getIdeaCount();
    }

    private void saveToFireBase() {
        Plan plan = mIdeaInteractor.getPlan();
        // TODO: uncomment this once the plan is saved to FireBase in prior to showing
        // ListCompositionFragment
//        mShoppingListDatabaseReference.child(plan.getId()).setValue(plan);
    }
}
