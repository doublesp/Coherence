package com.doublesp.coherence.actions;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;

import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Plan;
import com.doublesp.coherence.viewmodels.UserList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

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
    public void onFloatingAcitonButtonClick() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        StringBuilder sharableContentBuilder = new StringBuilder();
        Plan plan = mIdeaInteractor.getPlan();

        DatabaseReference keyReference = mListDatabaseReference.push();

        HashMap<String, Object> timestampCreated = new HashMap<>();
        timestampCreated.put(ConstantsAndUtils.TIMESTAMP, ServerValue.TIMESTAMP);
        UserList userList = new UserList(plan.getTitle(), plan.getOwner(), timestampCreated);
        keyReference.setValue(userList);

        mShoppingListDatabaseReference.child(keyReference.getKey()).setValue(plan);
        sharableContentBuilder
                .append("http://doublesp.com/shared/")
                .append(keyReference.getKey());

        // with app link
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharableContentBuilder.toString());
        mShareHandler.share(shareIntent);
    }

    @Override
    public void afterTextChanged(Editable s) {
        final int i = s.length();
        if (i == 0) {
            return;
        }
        if (s.subSequence(i - 1, i).toString().equals("\n")) {
            mIdeaInteractor.addIdea(s.toString().trim());
            s.clear();
        } else {
            mIdeaInteractor.getSuggestions(s.toString().trim());
        }
    }

    public interface IdeaShareHandlerInterface {
        void share(Intent intent);
    }

}
