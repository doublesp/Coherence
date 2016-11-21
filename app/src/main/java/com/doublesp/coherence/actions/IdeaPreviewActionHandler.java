package com.doublesp.coherence.actions;

import android.content.Intent;

import com.doublesp.coherence.R;
import com.doublesp.coherence.activities.HomeActivity;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaPreviewActionHandlerInterface;

/**
 * Created by pinyaoting on 11/14/16.
 */

public class IdeaPreviewActionHandler implements IdeaPreviewActionHandlerInterface {

    HomeActivity mActivity;
    IdeaInteractorInterface mIdeaInteractor;

    private final static String LIST_ID = "233333333";

    public IdeaPreviewActionHandler(HomeActivity activity, IdeaInteractorInterface ideaInteractor) {
        mActivity = activity;
        mIdeaInteractor = ideaInteractor;
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
                mIdeaInteractor.getSharableContent());    // TODO: Santhosh please save
        // mIdeaInteractor.getPlan() to Parse server
        sharableContentBuilder
                .append("http://doublesp.com/shared/")
                .append(LIST_ID);
        // with app link
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharableContentBuilder.toString());
        mActivity.share(shareIntent);
    }
}
