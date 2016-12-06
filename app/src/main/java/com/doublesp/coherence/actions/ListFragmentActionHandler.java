package com.doublesp.coherence.actions;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.doublesp.coherence.activities.ShareActivity;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import android.content.Context;
import android.content.Intent;

/**
 * Created by pinyaoting on 11/13/16.
 */

public class ListFragmentActionHandler implements ListFragmentActionHandlerInterface {

    Context mContext;
    IdeaShareHandlerInterface mShareHandler;
    IdeaInteractorInterface mIdeaInteractor;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListDatabaseReference;
    private DatabaseReference mShoppingListDatabaseReference;

    public ListFragmentActionHandler(Context context, IdeaInteractorInterface ideaInteractor) {
        mContext = context;
        if (context instanceof IdeaShareHandlerInterface) {
            mShareHandler = (IdeaShareHandlerInterface) context;
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.USER_LISTS)
                .child(ConstantsAndUtils.getOwner(context));
        mShoppingListDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.SHOPPING_LISTS);
        mIdeaInteractor = ideaInteractor;
    }

    @Override
    public void onShareButtonClick() {
        /*
        TODO: Sharing with sms, needs to be moved to another part or need to
        figure out how to do this.
        DatabaseReference keyReference = mListDatabaseReference.push();
        Plan plan = mIdeaInteractor.createPlan(keyReference.getKey());

        HashMap<String, Object> timestampCreated = new HashMap<>();
        timestampCreated.put(ConstantsAndUtils.TIMESTAMP, ServerValue.TIMESTAMP);
        UserList userList = new UserList(plan.getTitle(), plan.getOwner(), timestampCreated);
        keyReference.setValue(userList);

        mShoppingListDatabaseReference.child(keyReference.getKey()).setValue(plan);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        StringBuilder sharableContentBuilder = new StringBuilder();
        sharableContentBuilder
                .append("http://doublesp.com/shared/")
                .append(keyReference.getKey());

        // with app link
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharableContentBuilder.toString());
        mShareHandler.share(shareIntent);
        */

        Intent shareIntent = new Intent(mContext, ShareActivity.class);
        shareIntent.putExtra(ConstantsAndUtils.LIST_ID, mIdeaInteractor.getPlan().getId());
        mContext.startActivity(shareIntent);
    }

    @Override
    public void onSearchButtonClick() {
        Plan plan = mIdeaInteractor.getPlan();
        mShareHandler.search(plan);
    }

    @Override
    public void onCrossoutButtonClick(int pos) {
        Idea idea = mIdeaInteractor.getIdeaAtPos(pos);
        if (idea.isCrossedOut()) {
            mIdeaInteractor.uncrossoutIdea(pos);
        } else {
            mIdeaInteractor.crossoutIdea(pos);
        }
    }

    @Override
    public void onRemoveButtonClick(int pos) {
        mIdeaInteractor.removeIdea(pos);
    }

    public interface IdeaShareHandlerInterface {
        void share(Intent intent);

        void search(Plan plan);
    }
}
