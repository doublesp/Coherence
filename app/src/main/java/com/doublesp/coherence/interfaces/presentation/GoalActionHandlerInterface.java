package com.doublesp.coherence.interfaces.presentation;

/**
 * Created by pinyaoting on 11/26/16.
 */

public interface GoalActionHandlerInterface {

    void onPreviewButtonClick(int pos);

    interface PreviewHandlerInterface {
        void preview(int pos);
    }

}
