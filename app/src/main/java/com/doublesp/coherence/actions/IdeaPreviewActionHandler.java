package com.doublesp.coherence.actions;

import com.doublesp.coherence.activities.ListCompositionActivity;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.IdeaPreviewActionHandlerInterface;

import android.content.Intent;

/**
 * Created by pinyaoting on 11/14/16.
 */

public class IdeaPreviewActionHandler implements IdeaPreviewActionHandlerInterface {

    ListCompositionActivity mActivity;
    IdeaInteractorInterface mIdeaInteractor;

    public IdeaPreviewActionHandler(ListCompositionActivity activity, IdeaInteractorInterface ideaInteractor) {
        mActivity = activity;
        mIdeaInteractor = ideaInteractor;
    }

    @Override
    public void onFloatingAcitonButtonClick() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, mIdeaInteractor.getPlan());
        shareIntent.setType("*/*");
        mActivity.share(shareIntent);
    }
}
