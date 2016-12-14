package com.doublesp.coherence.adapters;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewholders.IdeaViewHolder;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import rx.Observer;

import static com.doublesp.coherence.fragments.ListCompositionFragment.binding;
import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

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

        mIdeaInteractor.subscribeIdeaStateChange(new Observer<ViewState>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ViewState state) {

                int start;
                int count;
                switch (state.getState()) {
                    case R.id.state_refreshing:
                        // TODO: reflect pending state on UI, maybe grey out the
                        // row and show a loding icon
                        break;
                    case R.id.state_loaded:
                        switch (state.getOperation()) {
                            case RELOAD:
                                notifyDataSetChanged();
                                if (mIdeaInteractor.getIdeaCount() == 0) {
                                    Snackbar.make(binding.rvIdeas,
                                            R.string.create_grocery_snackbar_hint,
                                            Snackbar.LENGTH_LONG).show();
                                }

                                saveToFireBase();
                                break;
                            case ADD:
                                start = state.getStart();
                                count = 1;
                                saveNewItemsToFireBase(start, count);
                                break;
                            case INSERT:
                                start = state.getStart();
                                count = state.getCount();
                                saveNewItemsToFireBase(start, count);
                                break;
                            case UPDATE:
                                start = state.getStart();
                                count = state.getCount();
                                updateItemInFireBase(start, count);
                                break;
                            case REMOVE:
                                saveToFireBase();
                                break;
                            case CLEAR:
                                break;
                        }
                        break;
                }

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
        View view = inflater.inflate(R.layout.item_idea, parent, false);
        IdeaViewHolder holder = new IdeaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Idea idea = mIdeaInteractor.getIdeaAtPos(position);
        if (holder instanceof IdeaViewHolder) {
            IdeaViewHolder ideaViewHolder = (IdeaViewHolder) holder;
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
        List<Idea> ideaList = plan.getIdeas();
        mShoppingListDatabaseReference.child(plan.getId()).child(ConstantsAndUtils.IDEAS).setValue(
                ideaList);
    }

    private void updateItemInFireBase(int start, int count) {
        Plan plan = mIdeaInteractor.getPlan();
        for (int i = 0; i < count; i++) {
            int pos = start + count - 1;
            Idea updatedIdea = mIdeaInteractor.getIdeaAtPos(pos);
            mShoppingListDatabaseReference.child(plan.getId()).child(ConstantsAndUtils.IDEAS)
                    .child(String.valueOf(pos)).setValue(updatedIdea);
        }
    }

    private void saveNewItemsToFireBase(int start, int count) {
        Plan plan = mIdeaInteractor.getPlan();
        for (int i = 0; i < count; i++) {
            int pos = start + count - 1;
            Idea newIdea = mIdeaInteractor.getIdeaAtPos(pos);
            mShoppingListDatabaseReference.child(plan.getId()).child(
                    ConstantsAndUtils.IDEAS).child(String.valueOf(pos)).setValue(newIdea);
        }
    }
}
