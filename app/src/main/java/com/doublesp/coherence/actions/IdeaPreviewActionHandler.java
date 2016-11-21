package com.doublesp.coherence.actions;

import android.content.Intent;

import com.doublesp.coherence.R;
import com.doublesp.coherence.activities.HomeActivity;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaPreviewActionHandlerInterface;
import com.doublesp.coherence.viewmodels.Plan;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by pinyaoting on 11/14/16.
 */

public class IdeaPreviewActionHandler implements IdeaPreviewActionHandlerInterface {

    HomeActivity mActivity;
    IdeaInteractorInterface mIdeaInteractor;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListDatabaseReference;

    public IdeaPreviewActionHandler(HomeActivity activity, IdeaInteractorInterface ideaInteractor) {
        mActivity = activity;
        mIdeaInteractor = ideaInteractor;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListDatabaseReference = mFirebaseDatabase.getReference().child("lists");
    }

    @Override
    public void onFloatingAcitonButtonClick() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        StringBuilder sharableContentBuilder = new StringBuilder(
                mActivity.getString(R.string.idea_share_cue));
        sharableContentBuilder.append("\n");
        sharableContentBuilder.append(
                mIdeaInteractor.getSharableContent());

        Plan plan = mIdeaInteractor.getPlan();
        DatabaseReference keyReference = mListDatabaseReference.push();
        keyReference.setValue(plan);
        sharableContentBuilder
                .append("http://doublesp.com/shared/")
                .append(keyReference.getKey());
        // with app link
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharableContentBuilder.toString());
        mActivity.share(shareIntent);
    }
}
