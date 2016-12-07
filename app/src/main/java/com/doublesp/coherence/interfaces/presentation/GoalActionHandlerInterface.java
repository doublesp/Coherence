package com.doublesp.coherence.interfaces.presentation;

import com.doublesp.coherence.viewholders.GoalViewHolder;

public interface GoalActionHandlerInterface {

    void onPreviewButtonClick(GoalViewHolder holder, int pos);

    interface PreviewHandlerInterface {
        void preview(GoalViewHolder holder, int pos);
    }

}
