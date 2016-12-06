package com.doublesp.coherence.interfaces.presentation;

public interface GoalActionHandlerInterface {

    void onPreviewButtonClick(int pos);

    interface PreviewHandlerInterface {
        void preview(int pos);
    }

}
