package com.doublesp.coherence.actions;

import com.doublesp.coherence.activities.HomeActivity;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaPreviewActionHandlerInterface;

import android.content.Intent;

/**
 * Created by pinyaoting on 11/14/16.
 */

public class IdeaPreviewActionHandler implements IdeaPreviewActionHandlerInterface {

    HomeActivity mActivity;
    IdeaInteractorInterface mIdeaInteractor;

    public IdeaPreviewActionHandler(HomeActivity activity, IdeaInteractorInterface ideaInteractor) {
        mActivity = activity;
        mIdeaInteractor = ideaInteractor;
    }

    @Override
    public void onFloatingAcitonButtonClick() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String sharableContent = mIdeaInteractor.getSharableContent();
        sharableContent = sharableContent + "https://github.com/doublesp/Coherence"; // TODO: Shawn replace this link with app link
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharableContent);
        mActivity.share(shareIntent);
    }
}
