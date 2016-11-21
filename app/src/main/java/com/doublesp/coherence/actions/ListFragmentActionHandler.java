package com.doublesp.coherence.actions;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;

import android.content.Context;
import android.content.Intent;

/**
 * Created by pinyaoting on 11/13/16.
 */

public class ListFragmentActionHandler implements ListFragmentActionHandlerInterface {

    Context mContext;
    IdeaShareHandlerInterface mShareHandler;
    IdeaInteractorInterface mIdeaInteractor;

    public ListFragmentActionHandler(Context context, IdeaInteractorInterface ideaInteractor) {
        mContext = context;
        if (context instanceof IdeaShareHandlerInterface) {
            mShareHandler = (IdeaShareHandlerInterface)context;
        }
        mIdeaInteractor = ideaInteractor;
    }

    @Override
    public void onFloatingAcitonButtonClick() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        StringBuilder sharableContentBuilder = new StringBuilder(mContext.getString(R.string.idea_share_cue));
        sharableContentBuilder.append("\n");
        sharableContentBuilder.append(mIdeaInteractor.getSharableContent());    // TODO: Santhosh please save mIdeaInteractor.getPlan() to Parse server
        sharableContentBuilder.append("https://goo.gl/TKoC6b");                 // TODO: Shawn please replace this link with app link
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharableContentBuilder.toString());
        mShareHandler.share(shareIntent);
    }

    public interface IdeaShareHandlerInterface {
        void share(Intent intent);
    }

}
