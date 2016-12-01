package com.doublesp.coherence.interfaces.presentation;

import android.text.Editable;

import com.doublesp.coherence.viewmodels.Goal;

/**
 * Created by pinyaoting on 11/26/16.
 */

public interface GoalActionHandlerInterface {

    void afterTextChanged(Editable s);

    void onBookmarkClick(int pos);

    void onPreviewButtonClick(int pos);

    interface PreviewHandlerInterface {
        void showPreviewDialog(Goal goal);
    }

}
